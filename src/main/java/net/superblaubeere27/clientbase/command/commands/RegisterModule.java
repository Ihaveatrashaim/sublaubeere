/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.superblaubeere27.clientbase.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.superblaubeere27.clientbase.ClientBase;
import net.superblaubeere27.clientbase.command.Command;
import net.superblaubeere27.clientbase.command.CommandException;
import net.superblaubeere27.clientbase.injection.interfaces.IMixinMinecraft;
import net.superblaubeere27.clientbase.utils.ChatUtils;
import net.superblaubeere27.clientbase.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class RegisterModule extends Command {

    public RegisterModule() {
        super("RegMod", "regMod", "rm");
    }

    @Override
    public void run(String alias, @NotNull String[] args) {
    	if(ClientBase.DEVMODE) {
    		ClientBase.INSTANCE.moduleManager.reloadModules();
    		throw new CommandException("Sucessfully reRegister Modules");
    	}else {
    		throw new CommandException("You need to have DEV Rank to do this");
    		}
    }

    @NotNull
    @Override
    public List<String> autocomplete(int arg, String[] args) {
        return new ArrayList<>();
    }
}
