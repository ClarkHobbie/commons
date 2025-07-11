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

import java.security.SecureRandom;
import java.util.Random;

/**
 * A subclass of {@link Random} that has some useful additions.
 * <p>
 *     Unlike {@link Random}, ImprovedRandom extends {@link SecureRandom}.  It provides methods like {@link #choose(Class, Object[])}
 *     and {@link #nextInt(int, int)}.
 * </p>
 *
 */
public class ImprovedRandom extends SecureRandom {
    private boolean wasNegative;

    public boolean getWasNegative () {
        return wasNegative;
    }

    public void setWasNegative(boolean wasNegative) {
        this.wasNegative = wasNegative;
    }

    public ImprovedRandom () {
    }

    public int nextIndex(int length) {
        int index = nextInt(length);

        if (index < 0)
            index = -1 * index;

        index = index % length;

        return index;
    }

    public int nextIndex(Object[] array) {
        return nextIndex(array.length);
    }

    public int nextInt (int atLeastAndIncluding, int upToAndIncluding)
    {
        int interval = upToAndIncluding - atLeastAndIncluding + 1;
        if (interval < 0) {
            throw new IllegalArgumentException("negative interval");
        }

        return atLeastAndIncluding + nextInt(interval);
    }

    public byte nextByte () {
        byte[] buffer = new byte[1];
        nextBytes(buffer);
        return buffer[0];
    }

    public long nextNonNegativeLong () {
        setWasNegative(false);
        long value = nextLong();

        if (value < 0) {
            value = -1 * value;
            setWasNegative(true);
        }

        return value;
    }

    public int nextNonNegativeInt () {
        setWasNegative(false);
        int value = nextInt();
        if (value < 0) {
            value = -1 * value;
            setWasNegative(true);
        }

        return value;
    }

    private String candidates = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`~!@#$%^&*()_-=+,./?;:'\"{}[]\\|,<.>";

    public char nextChar () {
        int index = nextIndex(candidates.length());
        return candidates.charAt(index);
    }

    public String randomString (int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = nextChar();
        }

        String result = chars.toString();
        return result;
    }

    public boolean nextBoolean () {
        int i = nextInt();
        if (i < 0)
            return false;
        else
            return true;
    }

    public <T> T choose (Class<T> clazz, Object[] candidates) {
        int index = this.nextInt(0, candidates.length - 1);
        return (T) candidates[index];
    }
}
