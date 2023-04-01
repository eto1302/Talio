package server.Services;

import commons.Board;
import commons.Color;
import commons.models.ColorEditModel;
import commons.models.IdResponseModel;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.ColorRepository;

@Service
public class ColorService {
    private ColorRepository colorRepository;
    private BoardRepository boardRepository;
    public ColorService(ColorRepository colorRepository, BoardRepository boardRepository){
        this.colorRepository = colorRepository;
        this.boardRepository = boardRepository;
    }

    public IdResponseModel saveColor(Color color) {
        try {
            colorRepository.save(color);
            return new IdResponseModel(color.getId(), null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }


    public IdResponseModel deleteColor(int colorId) {
        try {
            colorRepository.deleteById(colorId);
            return new IdResponseModel(colorId, null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public Color getColorById(int id){
        return colorRepository.getById(id);
    }

    public java.util.List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    public IdResponseModel editColor(int colorId, ColorEditModel model) {
        try {
            Color color = colorRepository.getById(colorId);
            color.setBackgroundColor(model.getBackgroundColor());
            color.setFontColor(model.getFontColor());
            color.setIsDefault(model.isDefault());
            colorRepository.save(color);
            return new IdResponseModel(colorId, null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }

    public IdResponseModel setToBoard(int colorId, int boardId){
        try {
            Board board = boardRepository.getById(boardId);
            Color color = colorRepository.getById(colorId);
            board.getColors().add(color);
            color.setBoard(board);
            color.setBoardId(boardId);
            colorRepository.save(color);
            return new IdResponseModel(color.getId(), null);
        } catch (Exception e) {
            return new IdResponseModel(-1, e.getMessage());
        }
    }
}
