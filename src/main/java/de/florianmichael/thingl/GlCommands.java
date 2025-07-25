/*
 * This file is part of ThinGL - https://github.com/RaphiMC/ThinGL
 * Copyright (C) 2024-2025 RK_01/RaphiMC and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.florianmichael.thingl;

import de.florianmichael.thingl.encoder.AppleCommandEncoder;
import de.florianmichael.thingl.encoder.GlCommandEncoder;
import net.raphimc.thingl.ThinGL;

public class GlCommands {

    private static final GlCommandEncoder instance;
    private static final boolean apple;

    static {
        apple = System.getProperty("os.name").toLowerCase().contains("mac");
        if (apple) {
            instance = new AppleCommandEncoder();
            ThinGL.LOGGER.warn("macOS detected, using limited features. Some features may not work as expected.");
        } else {
            instance = new GlCommandEncoder() {};
        }
    }

    public static GlCommandEncoder get() {
        return instance;
    }

    public static boolean isApple() {
        return apple;
    }

}
