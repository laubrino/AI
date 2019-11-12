package cz.laubrino.ai.patnact;

import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.activations.Activation;

/**
 * @author tomas.laubr on 12.11.2019.
 */
public class NNTest {
    @Test
    void testNN() {
        DenseLayer denseLayer = new DenseLayer.Builder().nIn(4).nOut(2).build();

        System.out.println(denseLayer.toString());

        NeuralNetConfiguration neuralNetConfiguration = new NeuralNetConfiguration.Builder()
                .activation(Activation.RELU)
                .layer(denseLayer)
                .build();

        System.out.println(neuralNetConfiguration);

    }
}
