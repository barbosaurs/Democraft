package me.barbosaur.nations;

import me.barbosaur.nations.commands.StateCommand;
import me.barbosaur.nations.commands.StateSubcommand;

import java.util.List;

public class NationsAPI {

    public static List<State> getStates(){
        return Nations.states;
    }

    public static List<State> getUnconfirmedStates(){
        return Nations.unconfirmedStates;
    }

    public static void addStateSubcommand(StateSubcommand subcommand, String command){
        StateCommand.subcmds.put(command, subcommand);
    }

}
