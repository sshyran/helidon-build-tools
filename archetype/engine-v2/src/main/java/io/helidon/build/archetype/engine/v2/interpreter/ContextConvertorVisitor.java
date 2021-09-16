/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
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

package io.helidon.build.archetype.engine.v2.interpreter;

import java.util.stream.Collectors;

class ContextConvertorVisitor extends GenericVisitorEmptyImpl<String, ASTNode> {

    @Override
    public String visit(ContextBooleanAST input, ASTNode arg) {
        return String.valueOf(input.bool());
    }

    @Override
    public String visit(ContextEnumAST input, ASTNode arg) {
        return input.value();
    }

    @Override
    public String visit(ContextListAST input, ASTNode arg) {
        return input.values().stream().collect(Collectors.joining("', '", "['", "']"));
    }

    @Override
    public String visit(ContextTextAST input, ASTNode arg) {
        String text = input.text();
        if (text.startsWith("'") && text.endsWith("'")) {
            return text;
        }
        return "'" + text + "'";
    }
}
