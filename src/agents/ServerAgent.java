package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ServerAgent extends Agent {
    private int magicNumber;
//    private Agent server;
    @Override
    protected void setup() {
        magicNumber = new Random().nextInt(10);
//        server = this;
        System.out.println("magicNumber" + magicNumber);
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if(msg != null){
                    System.out.println(msg.getSender().getName() + " " + msg.getContent());
                    ACLMessage msgResponse = new ACLMessage(ACLMessage.INFORM);
                    msgResponse.addReceiver(msg.getSender());
                    if(msg.getContent().equals("giveup")){
                        msgResponse.setContent("lose=>The magic number was " + magicNumber);
                    } else {
                        int nbr = Integer.parseInt(msg.getContent());
//                    msgResponse.setContent("msg reponse client: " + msg.getSender().getName());
//                    send(msgResponse);
                        if(nbr == magicNumber){
                            msgResponse.setContent("win");
                            AMSAgentDescription[] agents = null;
                            String localAgent = msg.getSender().getLocalName();
                            try {
                                SearchConstraints c = new SearchConstraints();
                                c.setMaxResults ((long)-1);
                                agents = AMSService.search(getAgent(), new AMSAgentDescription(), c);
                            }
                            catch (Exception e) {}
                            for (int i=0; i<agents.length;i++){

                                AID agentID = agents[i].getName();
                                String name= agentID.getLocalName();
                                if(!(name.equals("df")||name.equals("server")||name.equals("rma")||name.equals("ams")||name.equals(localAgent))){
                                    ACLMessage respondAll = new ACLMessage(ACLMessage.INFORM);
                                    respondAll.addReceiver(agentID);
                                    respondAll.setContent("lose=>The magic number was " + magicNumber + "\n" + localAgent + " has found it");
                                    send(respondAll);
                                }
                                System.out.println(agentID.getLocalName());
                            }
                        }else if(nbr < magicNumber) {
                            msgResponse.setContent("Entrez un nombre superieur");
                        }else {
                            msgResponse.setContent("Entrez un nombre inferieur");
                        }
                    }
                    send(msgResponse);
                }else{
                    block();
                }
            }
        });
    }
}
