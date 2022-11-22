package bridge;

import bridge.view.InputView;
import bridge.view.OutputView;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private static final InputView inputView = new InputView();
    private static final OutputView outputView = new OutputView();
    private static final BridgeGame bridgeGame = new BridgeGame();

    private static List<String> bridge;
    private static int bridgeSize;
    private static List<String> upBridgeResult;
    private static List<String> downBridgeResult;
    private static String gameStatus;
    private static int location;
    private static int cnt;

    public static void main(String[] args) {
        // TODO: 프로그램 구현
        outputView.printGameStart();
        startMakingBridge();
        cnt = 0;
        location = 0;
        do{ upBridgeResult = new ArrayList<>();
            downBridgeResult = new ArrayList<>();
            gameStatus = "성공";
            playGame();
        } while(retryOrQuit());
        outputView.printResult(upBridgeResult, downBridgeResult, gameStatus, cnt);
    }

    public static void startMakingBridge() {
        try{
            outputView.printBridgeSize();
            bridgeSize = inputView.readBridgeSize();
            BridgeMaker bridgeMaker = new BridgeMaker(new BridgeRandomNumberGenerator());
            bridge = bridgeMaker.makeBridge(bridgeSize);
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
            startMakingBridge();
        }
    }

    public static boolean retryOrQuit(){
        if (endGame()) return false;
        try {
            outputView.printGameCommand();
            if (inputView.readGameCommand().equals("R")){
                bridgeGame.retry(location, upBridgeResult, downBridgeResult);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
            retryOrQuit();
        }
        return true;
    }
    public static boolean endGame(){
        if (gameStatus.equals("성공")){
            return true;
        }
        return false;
    }

    public static void playGame(){
        cnt++;
        try {
            location = movePlayer();
        } catch (IllegalArgumentException e){
            playGame();
            outputView.printError(e.getMessage());
        }
    }
    public static int movePlayer() {
        do{
            outputView.printMoving();
            String moving = inputView.readMoving();
            gameStatus = bridgeGame.crossResult(moving, location, bridge, upBridgeResult, downBridgeResult);
            if (gameStatus.equals("성공")){
                location = bridgeGame.move(location);
            }
            outputView.printCrossResult(upBridgeResult, downBridgeResult);
        } while (location < bridgeSize && gameStatus.equals("성공"));
        return location;
    }

}