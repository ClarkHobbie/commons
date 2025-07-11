/*
 * Copyright 2017 Long Term Software LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ltsllc.commons.util;

/**
 * A class that represents a property.
 */
public class Property {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public boolean equals (Object o) {
        if (null == o || !(o instanceof Property))
            return false;

        Property other = (Property) o;

        return getName().equals(other.getName()) && getValue().equals(other.getValue());
    }
}
