/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.security.management.search;

import org.jboss.errai.security.shared.api.Group;

import javax.enterprise.context.ApplicationScoped;

/**
 * <p>Default runtime search engine implementation for collections of groups.</p>
 * 
 * @since 0.8.0
 */
@ApplicationScoped
public class GroupsRuntimeSearchEngine extends AbstractRuntimeSearchEngine<Group> {

    @Override
    protected String getIdentifier(Group entity) {
        return entity.getName();
    }
}
