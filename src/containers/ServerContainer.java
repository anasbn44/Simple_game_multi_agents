package containers;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class ServerContainer {
    public static void main(String[] args) throws Exception {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl(false);
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer serverContainer = runtime.createAgentContainer(profile);
        AgentController serverAgent = serverContainer.createNewAgent("server", "agents.ServerAgent", new Object[]{});
        serverAgent.start();
    }
}
