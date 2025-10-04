package com.example.portfolio.servivce.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ib.client.EJavaSignal;
import com.ib.client.EClientSocket;

@Service
public class IbkrConnectionService {

    private final EClientSocket client;
    private final IbkrEWrapper wrapper;
    private final EJavaSignal signal;

    private final String host;
    private final int port;
    private final int clientId;

    public IbkrConnectionService(@Value("${ibkr.gateway.host}") String host,
                                 @Value("${ibkr.gateway.port}") int port,
                                 @Value("${ibkr.gateway.clientId}") int clientId){

        this.wrapper = new IbkrEWrapper();
        this.signal = new EJavaSignal();
        this.client = new EClientSocket(wrapper, signal);

        this.host = host;
        this.port = port;
        this.clientId = clientId;
    }

    void connectToIbkr() {
        client.eConnect(host, port, clientId);
        System.out.println("IBKR connection set: "+ client.isConnected());
    }

    void getIbkrTransactions(){
        if (!client.isConnected()){

            client.eConnect(host, port, clientId);
            client.reqCompletedOrders(false);

        }else{

            client.reqCompletedOrders(false);

        }

    }
}
