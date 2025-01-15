//package org.AAKB.server;
//
//import java.util.*;
//import java.util.concurrent.atomic.AtomicInteger;
//
//
//public abstract class AbstractClientHandler extends Thread {
//
//    private static final ArrayList<Integer> allowedNumberOfPlayers = new ArrayList<>(Arrays.asList(2,3,4,6));
//
//    protected static final Set<ClientHandler> clientHandlers = new HashSet<>();
//
//    protected static Map<ClientHandler, Integer> playerIds = new HashMap<>();
//
//    protected static AtomicInteger totalPlayers = new AtomicInteger(0);
//
//    protected static AtomicInteger currentPlayers = new AtomicInteger(0);
//
//    protected static volatile boolean gameStarted = false;
//
//    protected static int currentTurn = 0;
//
//    public AbstractClientHandler(){}
//
//    protected boolean validateNumberOfPlayers(int number){
//
//        return allowedNumberOfPlayers.contains(number);
//    }
//
//    @Override
//    public abstract void run();
//}
