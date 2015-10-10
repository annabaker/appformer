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

package org.uberfire.ext.security.management.client.widgets.management.events;

import org.uberfire.workbench.events.UberFireEvent;

import java.util.Set;

/**
 * <p>A user is updating assigned user's groups.</p>
 * 
 * @since 0.8.0
 */
public class OnUpdateUserGroupsEvent extends ContextualEvent implements UberFireEvent {

    private Set<String> groups;

    public OnUpdateUserGroupsEvent(Object context, Set<String> groups) {
        super(context);
        this.groups = groups;
    }

    public Set<String> getGroups() {
        return groups;
    }
}
