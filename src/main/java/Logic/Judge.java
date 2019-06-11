package Logic;

public class Judge {

    public static int numberOfWin = 3;


    public static Sign getVerdict(int lastMove, Sign[][] resultMatrix){

        int numberOfRows = resultMatrix.length;
        int row = lastMove/numberOfRows;
        int column = lastMove%numberOfRows;
        System.out.println("("+row+","+column+")");
        int circles = 0;
        int crosses = 0;
        Sign sign;
        // column
        for(int i=0;i<numberOfRows;i++){
            sign = resultMatrix[row][i];
            switch (sign){
                case CIRCLE:
                    circles+=1;
                    crosses=0;
                    break;
                case CROSS:
                    crosses+=1;
                    circles=0;
                    break;
                default:
                    crosses=0;
                    circles=0;
            }
            if(crosses>=numberOfWin) return Sign.CROSS;
            if(circles>=numberOfWin) return Sign.CIRCLE;

        }
        circles = 0;
        crosses = 0;
        // row
        for(int i=0;i<numberOfRows;i++){
            sign = resultMatrix[i][column];
            switch (sign){
                case CIRCLE:
                    circles+=1;
                    crosses=0;
                    break;
                case CROSS:
                    crosses+=1;
                    circles=0;
                    break;
                default:
                    crosses=0;
                    circles=0;
            }
            if(crosses>=numberOfWin) return Sign.CROSS;
            if(circles>=numberOfWin) return Sign.CIRCLE;
        }
        int min = numberOfRows - numberOfWin;
        if(row>=numberOfWin && column>=numberOfRows && row<=min && column<=min){
            System.out.println("JEST");
        }

        int sum = row+column;
        int diff = row-column;
//        //na ukos 1 (od dołu do góry na prawo jest okey)
//        if(sum>numberOfWin-2 && sum<(numberOfRows-1)*2+2-numberOfWin ){
//            circles = 0;
//            crosses = 0;
//            // row
//            int i=row;
//            int j=0;
//
//            while(i>=0 && j<=numberOfRows){
//                System.out.println("sprawdzam -> "+"("+i+","+j+")");
//                sign = resultMatrix[i][j];
//                switch (sign){
//                    case CIRCLE:
//                        circles+=1;
//                        crosses=0;
//                        break;
//                    case CROSS:
//                        crosses+=1;
//                        circles=0;
//                        break;
//                    default:
//                        crosses=0;
//                        circles=0;
//                }
//                if(crosses>=numberOfWin) return Sign.CROSS;
//                if(circles>=numberOfWin) return Sign.CIRCLE;
//                i--;
//                j++;
//            }
//        }
//        //na ukos 2
//        if(diff<=numberOfRows-numberOfWin && diff>=-(numberOfRows-numberOfWin )){
//
//
//        }
        return Sign.NONE;
    }


}
