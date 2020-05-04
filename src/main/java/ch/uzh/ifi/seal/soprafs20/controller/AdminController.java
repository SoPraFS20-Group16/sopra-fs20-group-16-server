package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.ApplicationConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);


    private final GameService gameService;
    private final MoveService moveService;

    public AdminController(GameService gameService, MoveService moveService) {
        this.gameService = gameService;
        this.moveService = moveService;
    }

    @GetMapping("/admin")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String admin(@RequestParam(name = "password") String adminPassword,
                        @RequestParam(name = "game") Long gameId) {

        if (!adminPassword.equals(ApplicationConstants.ADMIN_PASSWORD)) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Wrong admin password");
        }

        Game game = gameService.findGameById(gameId);
        List<Move> moves = moveService.findMovesForGameId(gameId);

        if (game != null) {
            ObjectMapper mapper = new ObjectMapper();

            String mappedAnswer;

            try {

                //Remove coordinate neighbors
                game.getBoard().getTiles().forEach(tile -> tile.getCoordinates()
                        .forEach(coordinate -> coordinate.setNeighbors(new ArrayList<>())));

                //Map moves to dtos
                List<MoveDTO> moveDTOs = new ArrayList<>();
                moves.forEach(move -> moveDTOs.add(move.getMoveHandler().mapToDTO(move)));

                Map<String, Object> answer = new HashMap<>();
                answer.put("game", game);
                answer.put("moves", moveDTOs);

                mappedAnswer = mapper.writeValueAsString(answer);
            }
            catch (Exception e) {
                log.error("Exception during mapping", e);
                return "Problem while mapping to json";
            }
            return mappedAnswer;
        }
        return "The game was null!";
    }
}
