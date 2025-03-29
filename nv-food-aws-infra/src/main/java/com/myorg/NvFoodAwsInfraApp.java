package com.myorg;

import software.amazon.awscdk.App;

public class NvFoodAwsInfraApp {
    public static void main(final String[] args) {
        App app = new App();

        NvFoodVpcStack vpcStack = new NvFoodVpcStack(app, "Vpc");

        NvFoodClusterStack clusterStack = new NvFoodClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        NvFoodRdsPedidosStack rdsStack = new NvFoodRdsPedidosStack(app, "Rds-pedidos-ms", vpcStack.getVpc());
        rdsStack.addDependency(vpcStack);

        NvFoodServicePedidosStack serviceStack = new NvFoodServicePedidosStack(app, "Service", clusterStack.getCluster());
        serviceStack.addDependency(clusterStack);
        serviceStack.addDependency(rdsStack);

        app.synth();
    }
}