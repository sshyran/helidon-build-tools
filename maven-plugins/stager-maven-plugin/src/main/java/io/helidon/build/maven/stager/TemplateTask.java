/*
 * Copyright (c) 2020, 2022 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.helidon.build.maven.stager;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.util.DecoratedCollection;

import static java.util.stream.Collectors.toMap;

/**
 * Render a mustache template.
 */
final class TemplateTask extends StagingTask {

    static final String ELEMENT_NAME = "template";

    private final String source;
    private final Map<String, Object> templateVariables;

    TemplateTask(ActionIterators iterators, String source, String target, List<Variable> variables) {
        super(iterators, target);
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("source is required");
        }
        this.source = source;
        this.templateVariables = variables.stream().collect(toMap(Variable::name, TemplateTask::mapValue));
    }

    /**
     * Get the source.
     *
     * @return source, never {@code null}
     */
    String source() {
        return source;
    }

    @Override
    public String elementName() {
        return ELEMENT_NAME;
    }

    /**
     * Get the variables.
     *
     * @return map of variable values, never {@code null}
     */
    Map<String, Object> templateVariables() {
        return templateVariables;
    }

    @Override
    protected void doExecute(StagingContext context, Path dir, Map<String, String> variables) throws IOException {
        String resolvedTarget = resolveVar(target(), variables);
        String resolvedSource = resolveVar(source, variables);
        Path sourceFile = context.resolve(resolvedSource);
        if (!Files.exists(sourceFile)) {
            throw new IllegalStateException(sourceFile + " does not exist");
        }
        Path targetFile = dir.resolve(resolvedTarget);
        Files.createDirectories(targetFile.getParent());
        try (Reader reader = Files.newBufferedReader(sourceFile);
             Writer writer = Files.newBufferedWriter(targetFile,
                     StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            new DefaultMustacheFactory().compile(reader, resolvedSource)
                                        .execute(writer, templateVariables)
                                        .flush();
        }
    }

    @Override
    public String describe(Path dir, Map<String, String> variables) {
        return ELEMENT_NAME + "{"
                + "source=" + resolveVar(source, variables)
                + ", target=" + resolveVar(target(), variables)
                + ", vars" + templateVariables
                + '}';
    }

    private static Object mapValue(Variable variable) {
        VariableValue value = variable.value();
        if (value instanceof VariableValue.ListValue) {
            return new DecoratedCollection<>(((VariableValue.ListValue) value).unwrap());
        }
        return value.unwrap();
    }
}
