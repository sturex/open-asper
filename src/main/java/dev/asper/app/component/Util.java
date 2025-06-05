package dev.asper.app.component;


import dev.asper.poker.engine.PokerEngine;
import dev.asper.poker.engine.PokerHandReplayer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Util {

    @Bean
    public PokerEngine pokerEngine() {
        return new PokerEngine();
    }

    @Bean
    public PokerHandReplayer pokerHandReplayer(PokerEngine pokerEngine) {
        return new PokerHandReplayer(pokerEngine);
    }

}
