/**
 * 
 */
package ru.jimbot.anekbot.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import ru.jimbot.anekbot.AnekBot;
import ru.jimbot.anekbot.AnekBotCommandParser;
import ru.jimbot.core.DefaultCommand;
import ru.jimbot.core.Message;
import ru.jimbot.core.Parser;
import ru.jimbot.core.exceptions.DbException;

/**
 * Случайный анекдот
 * 
 * @author Prolubnikov Dmitry
 */
public class Cmd1 extends DefaultCommand {

	public Cmd1(Parser p) {
        super(p);
    }
	
	/* (non-Javadoc)
	 * @see ru.jimbot.core.Command#exec(ru.jimbot.core.Message)
	 */
	@Override
	public Message exec(Message m) {
		return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), null));
	}

	/* (non-Javadoc)
	 * @see ru.jimbot.core.Command#exec(java.lang.String, java.util.Vector)
	 */
	@Override
	public String exec(String sn, Vector param) {
		((AnekBotCommandParser)p).state++;
        ((AnekBotCommandParser)p).stateInc(sn);
        try {
			return ((AnekBot)p.getService()).getAnekDB().getAnek();
		} catch (Exception e) {
			p.getService().err(e.getMessage(), e);
			return "Ошибка получения анекдота :(";
		}
	}

	/* (non-Javadoc)
	 * @see ru.jimbot.core.Command#getCommandPatterns()
	 */
	@Override
	public List<String> getCommandPatterns() {
		return Arrays.asList(new String[] {"1"});
	}

	/* (non-Javadoc)
	 * @see ru.jimbot.core.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return " - случайный анекдот";
	}

	/* (non-Javadoc)
	 * @see ru.jimbot.core.Command#getXHelp()
	 */
	@Override
	public String getXHelp() {
		return getHelp();
	}

}