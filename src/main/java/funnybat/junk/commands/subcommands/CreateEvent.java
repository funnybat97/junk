package funnybat.junk.commands.subcommands;

import funnybat.junk.DBHandler;

import funnybat.junk.commands.SubCommand;
import org.bukkit.entity.Player;
public class CreateEvent extends SubCommand {
    public DBHandler DB;
    public CreateEvent(DBHandler DB){
        DB = DB;
    }
    @Override
    public String getName() {
        return "create_event";
    }

    @Override
    public String getDescription() {
        return "Create event with name";
    }

    @Override
    public String getSyntax() {
        return "/junk_event create_event <event name>";
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length > 1 && player.isOp()) {

            String name = args[1];
            DB.create_event(name);
            player.sendMessage("You created event!");

        }else if(args.length == 1){
            player.sendMessage("You did not provide a name!");
            player.sendMessage("Do it like this: /junk_event create_event test_event");
        }


    }

}
