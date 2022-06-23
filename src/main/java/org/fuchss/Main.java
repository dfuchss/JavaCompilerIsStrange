package org.fuchss;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SimpleTreeVisitor;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.Trees;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception {

		var javac = ToolProvider.getSystemJavaCompiler();
		var listener = new DiagnosticCollector<>();

		try (final StandardJavaFileManager fileManager = javac.getStandardFileManager(listener, null, StandardCharsets.UTF_8)) {
			var javaFiles = fileManager.getJavaFileObjectsFromFiles(List.of(new File("src/main/resources/Example.java")));

			final JavaCompiler.CompilationTask task = javac.getTask(null, fileManager, listener, null, null, javaFiles);
			final Trees trees = Trees.instance(task);
			final SourcePositions positions = trees.getSourcePositions();
			for (final CompilationUnitTree ast : executeCompilationTask(task)) {
				final String filename = ast.getSourceFile().getName();
				final LineMap map = ast.getLineMap();

				// TODO Do something here ..
				// ast.accept(...);
			}
		}

		for (var errors : listener.getDiagnostics())
			System.out.println(errors);

		if (listener.getDiagnostics().isEmpty())
			throw new Error("Shall not be possible to compile.");
	}

	private static Iterable<? extends CompilationUnitTree> executeCompilationTask(final JavaCompiler.CompilationTask task) {
		Iterable<? extends CompilationUnitTree> abstractSyntaxTrees = Collections.emptyList();
		try {
			abstractSyntaxTrees = ((JavacTask) task).parse();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return abstractSyntaxTrees;
	}

}