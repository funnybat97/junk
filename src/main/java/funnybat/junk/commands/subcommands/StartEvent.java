package funnybat.junk.commands.subcommands;

import funnybat.junk.DBHandler;
import funnybat.junk.commands.SubCommand;
import org.bukkit.entity.Player;

public class StartEvent extends SubCommand {

    public DBHandler DB;
    public StartEvent(DBHandler DB){
        DB = DB;
    }
    @Override
    public String getName() {
        return "start_event";
    }


    @Override
    public String getDescription() {
        return "Start event with name";
    }

    @Override
    public String getSyntax() {
        return "/junk_event start_event <event name>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1 && player.isOp()) {

            String name = args[1];
            DB.start_event(name);
            player.sendMessage("You started event!");

        }else if(args.length == 1){
            player.sendMessage("You did not provide a name!");
            player.sendMessage("Do it like this: /junk_event start_event test_event");
        }


    }

}
