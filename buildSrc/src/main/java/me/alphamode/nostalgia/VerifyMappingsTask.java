package me.alphamode.nostalgia;

import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MemoryMappingTree;
import net.fabricmc.tinyremapper.IMappingProvider;
import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;

import net.ornithemc.keratin.KeratinGradleExtension;
import net.ornithemc.keratin.api.MinecraftVersion;
import net.ornithemc.keratin.api.task.MinecraftTask;
import net.ornithemc.keratin.files.GlobalCache;
import net.ornithemc.keratin.files.KeratinFiles;
import net.ornithemc.keratin.files.MappingsDevelopmentFiles;
import org.gradle.workers.WorkQueue;

import java.io.File;
import java.io.IOException;

public abstract class VerifyMappingsTask extends MinecraftTask {
	@Override
	protected void run(WorkQueue workQueue, MinecraftVersion minecraftVersion) throws Exception {
		KeratinGradleExtension keratin = getExtension();
		KeratinFiles files = keratin.getFiles();

		GlobalCache.MappingsCache mappings = files.getGlobalCache().getMappingsCache();
		MappingsDevelopmentFiles.BuildFiles buildFiles = files.getMappingsDevelopmentFiles().getBuildFiles();

		GlobalCache globalCache = files.getGlobalCache();
		GlobalCache.GameJarsCache gameJars = globalCache.getGameJarsCache();
		GlobalCache.LibrariesCache libraries = globalCache.getLibrariesCache();

		MemoryMappingTree mappingTree = new MemoryMappingTree();
		MappingReader.read(buildFiles.getMergedTinyV2MappingsFile(minecraftVersion.id()).toPath(), mappingTree);
		TinyRemapper remapper = TinyRemapper.newRemapper()
				.withMappings(create(mappingTree, "official", "named", true)).build();

		try(OutputConsumerPath outputConsumer = new OutputConsumerPath.Builder(buildFiles.getNamedJar(minecraftVersion.id()).toPath()).build()) {
			outputConsumer.addNonClassFiles(gameJars.getMergedJar(minecraftVersion).toPath());
			remapper.readInputs(gameJars.getMergedJar(minecraftVersion).toPath());
			for (File library : libraries.getLibraries(minecraftVersion)) {
				remapper.readClassPath(library.toPath());
			}
			remapper.apply(outputConsumer);
			outputConsumer.close();
			remapper.finish();
		} catch (Exception e) {
			remapper.finish();
			throw new IOException("Failed to remap jar", e);
		}
	}

	private static IMappingProvider.Member memberOf(String className, String memberName, String descriptor) {
		return new IMappingProvider.Member(className, memberName, descriptor);
	}

	public static IMappingProvider create(MappingTree mappings, String from, String to, boolean remapLocalVariables) {
		return (acceptor) -> {
			final int fromId = mappings.getNamespaceId(from);
			final int toId = mappings.getNamespaceId(to);

			for (MappingTree.ClassMapping classDef : mappings.getClasses()) {
				String className = classDef.getName(fromId);

				if (className == null) {
					continue;
				}

				String dstClassName = classDef.getName(toId);

				if (dstClassName == null) {
					// Unsure if this is correct, should be better than crashing tho.
					dstClassName = className;
				}

				acceptor.acceptClass(className, dstClassName);

				for (MappingTree.FieldMapping field : classDef.getFields()) {
					String fieldName = field.getName(fromId);

					if (fieldName == null) {
						continue;
					}

					String dstFieldName = field.getName(toId);

					if (dstFieldName == null) {
						dstFieldName = fieldName;
					}

					acceptor.acceptField(memberOf(className, fieldName, field.getDesc(fromId)), dstFieldName);
				}

				for (MappingTree.MethodMapping method : classDef.getMethods()) {
					String methodName = method.getName(fromId);

					if (methodName == null) {
						continue;
					}

					String dstMethodName = method.getName(toId);

					if (dstMethodName == null) {
						dstMethodName = methodName;
					}

					IMappingProvider.Member methodIdentifier = memberOf(className, methodName, method.getDesc(fromId));
					acceptor.acceptMethod(methodIdentifier, dstMethodName);

					if (remapLocalVariables) {
						for (MappingTree.MethodArgMapping parameter : method.getArgs()) {
							String name = parameter.getName(toId);

							if (name == null) {
								continue;
							}

							acceptor.acceptMethodArg(methodIdentifier, parameter.getLvIndex(), name);
						}

						for (MappingTree.MethodVarMapping localVariable : method.getVars()) {
							acceptor.acceptMethodVar(methodIdentifier, localVariable.getLvIndex(),
									localVariable.getStartOpIdx(), localVariable.getLvtRowIndex(),
									localVariable.getName(toId));
						}
					}
				}
			}
		};
	}
}
