package funnybat.junk.commands.subcommands;

import funnybat.junk.DBHandler;
import funnybat.junk.commands.SubCommand;
import org.bukkit.entity.Player;

public class StopEvent extends SubCommand {

    public DBHandler DB;
    public StopEvent(DBHandler DB){
        DB = DB;
    }
    @Override
    public String getName() {
        return "stop_event";
    }

    @Override
    public String getDescription() {
        return "Start event with name";
    }

    @Override
    public String getSyntax() {
        return "/junk_event stop_event <event name>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1 && player.isOp()) {

            String name = args[1];
            DB.stop_event(name);
            player.sendMessage("You stopped event!");

        }else if(args.length == 1){
            player.sendMessage("You did not provide a name!");
            player.sendMessage("Do it like this: /junk_event stop_event test_event");
        }


    }

}
