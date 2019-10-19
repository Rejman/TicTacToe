package Models.Game;

import java.util.Objects;

public class Position {
    public int column;
    public int row;

    public Position(int row, int column) {
        this.column = column;
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return column == position.column &&
                row == position.row;
    }

    public static int convertToNumber(Position position, int matrixDegree){
        return position.row*matrixDegree+position.column;
    }
    public static Position convertToPositon(int number, int matrixDegree){

        int row = number/matrixDegree;
        int column = number%matrixDegree;

        return new Position(row,column);
    }

    public static void main(String[] args) {
        System.out.println("Test Postion");
        for(int i=0;i<25;i++){
            Position pos = convertToPositon(i,5);
            System.out.println(pos.row+", "+pos.column);
        }
        System.out.println("---------------------------------");
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                Position position = new Position(i,j);
                int field = Position.convertToNumber(position, 5);
                System.out.println(field);
            }
        }

    }
}
