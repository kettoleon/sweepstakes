package com.github.kettoleon.sweepstakes.bet.controller;

import com.github.kettoleon.sweepstakes.bet.repo.FixtureBet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BetsForm {

    private List<FixtureBet> bets = new ArrayList<>();

    public boolean hasMissingBets(){
        if(areAllBetsMissing()){
            return false;
        }
        return isAnyBetMissing();
    }

    public boolean isAnyBetMissing(){
        return !areAllBetsPlaced();
    }
    public boolean areAllBetsPlaced(){
        for(FixtureBet fb : bets){
            if(!fb.isPlaced()){
                return false;
            }
        }
        return true;
    }
    public boolean areAllBetsMissing() {
        for(FixtureBet fb : bets){
            if(fb.isPlaced()){
                return false;
            }
        }
        return true;
    }

}
