package Logic;

public class Judge {

    public static int numberOfRows = 3;
    public static int numberOfWin = 3;


    public static Sign getVerdict(int lastMove, Sign[][] resultMatrix){
        int row = lastMove/numberOfRows;
        int column = lastMove%numberOfRows;
        int circle = 0;
        int cross = 0;
        Sign sign;

        for(int i=0;i<numberOfRows;i++){
            sign = resultMatrix[row][i];
            switch (sign){
                case CIRCLE:
                    circle+=1;
                    cross=0;
                    break;
                case CROSS:
                    cross+=1;
                    circle=0;
                    break;
                default:
                    cross=0;
                    circle=0;
            }
            if(cross>=numberOfWin) return Sign.CROSS;
            if(circle>=numberOfWin) return Sign.CIRCLE;
        }

        for(int i=0;i<numberOfRows;i++){
            sign = resultMatrix[i][column];
            switch (sign){
                case CIRCLE:
                    circle+=1;
                    cross=0;
                    break;
                case CROSS:
                    cross+=1;
                    circle=0;
                    break;
                default:
                    cross=0;
                    circle=0;
            }
            if(cross>=numberOfWin) return Sign.CROSS;
            if(circle>=numberOfWin) return Sign.CIRCLE;
        }

        return Sign.NONE;
    }
}
