package ServerUtil;

import com.jcraft.jsch.*;
import java.io.*;

public class SSHCommandExecutor {

    private String host;
    private String username;
    private String passphrase;
    private int port;

    public SSHCommandExecutor(String host, String username, String passphrase, int port) {
        this.host = host;
        this.username = username;
        this.passphrase = passphrase;
        this.port = port;
    }

    public void executeCommand(String command) {
        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(username, host, port);
            session.setPassword(passphrase);
//            // Avoid checking host key (you should only do this in a trusted network)
            session.setConfig("StrictHostKeyChecking", "no");

            // Connect to the SSH server
            session.connect();

            // Create a SSH channel
            Channel channel = session.openChannel("exec");

            // Specify the command you want to execute
            ((ChannelExec) channel).setCommand(command);

            // Set up input and output streams
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            // Connect to the channel and execute the command
            channel.connect();

            // Read the output of the command
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {
                System.out.write(buffer, 0, bytesRead);
            }

            // Disconnect from the channel and session
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
    }
}
