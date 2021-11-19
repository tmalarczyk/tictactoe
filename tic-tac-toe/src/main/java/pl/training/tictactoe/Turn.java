package pl.training.tictactoe;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Turn {

    private String sender;
    private String recipient;
    private String text;
    private LocalTime timestamp;

}
