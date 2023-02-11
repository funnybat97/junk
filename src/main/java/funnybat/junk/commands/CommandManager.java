package funnybat.junk.commands;

import funnybat.junk.DBHandler;
import funnybat.junk.commands.subcommands.CreateEvent;
import funnybat.junk.commands.subcommands.StartEvent;
import funnybat.junk.commands.subcommands.StopEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class CommandManager implements CommandExecutor {
    public DBHandler DB;
    private ArrayList<SubCommand> subcommands = new ArrayList<>();
    public CommandManager(DBHandler DB){
        DB = DB;
        subcommands.add(new CreateEvent(DB));
        subcommands.add(new StartEvent(DB));
        subcommands.add(new StopEvent(DB));
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;

            if (args.length > 0){
                for (int i = 0; i < getSubcommands().size(); i++){
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                        getSubcommands().get(i).perform(p, args);
                    }
                }
            }else if(args.length == 0){
                p.sendMessage("--------------------------------");
                for (int i = 0; i < getSubcommands().size(); i++){
                    p.sendMessage(getSubcommands().get(i).getSyntax() + " - " + getSubcommands().get(i).getDescription());
                }
                p.sendMessage("--------------------------------");
            }

        }


        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }


}

