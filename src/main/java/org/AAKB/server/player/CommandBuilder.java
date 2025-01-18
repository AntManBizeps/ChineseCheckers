package org.AAKB.server.player;

public class CommandBuilder {
    private StringBuilder command;

    public CommandBuilder()
    {
        command = new StringBuilder();
    }
    /**
     * Dodaje dany kod do komendy i rozdziela go znakiem '@' od innych
     */
    public void addCommand(String code)
    {
        if (command.toString().isEmpty())
        {
            command.append(code);
        } else
        {
            command.append("@");
            command.append(code);
        }
    }

    /**
     * Dodaje kod wraz z jego danymi do komendy
     */
    public void addCommand(String code, String data)
    {
        if (command.toString().isEmpty())
        {
            command.append(code);
            command.append(" ");
            command.append(data);
        } else
        {
            command.append("@");
            command.append(code);
            command.append(" ");
            command.append(data);
        }

    }

    /**
     * zwraca utworzoną komendę
     */
    String getCommand()
    {
        return command.toString();
    }
}
