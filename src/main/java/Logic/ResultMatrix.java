package Logic;

import Logic.Sign;

import java.util.LinkedList;
import java.util.List;

public class ResultMatrix {
    public int degree;
    public Sign[][] values;

    public ResultMatrix(int degree) {
        this.degree = degree;
        this.values = new Sign[degree][degree];
        clearMatrix();

    }
    public void clearMatrix(){
        for(int i=0;i<degree;i++){
            for(int j=0;j<degree;j++){
                values[i][j] = Sign.NONE;
            }
        }
    }
    public Sign[] getRow(int number){
        return values[number];
    }
    public Sign[] getColumn(int number){
        Sign[] column = new Sign[degree];
        for(int i=0;i<degree;i++){
            column[i] = values[i][number];
        }
        return column;
    }
    public Sign[] findRow(Position pos){
        return getRow(pos.row);
    }
    public Sign[] findRow(int field){
        Position pos = Position.convertToPositon(field, degree);
        return getRow(pos.row);
    }
    public Sign[] findColumn(Position pos){
        return getColumn(pos.column);
    }
    public Sign[] findColumn(int field){
        Position pos = Position.convertToPositon(field, degree);
        return getColumn(pos.column);
    }

    public List findFallingDiagonal(Position pos){
        Position start = getStartPositionUp(pos);
        List<Sign> diagonal = new LinkedList<Sign>();

        int column=start.column;
        int row=start.row;
        while (row<degree && column<degree){
            diagonal.add(values[row][column]);
            row++;
            column++;
        }
        return diagonal;
    }
    public List findGrowingDiagonal(Position pos){
        Position start = getStartPositionDown(pos);
        List<Sign> diagonal = new LinkedList<Sign>();
        System.out.println(start.row+", "+start.column);
        int row=start.row;
        int column=start.column;

        while (row>=0 && column<degree){
            diagonal.add(values[row][column]);
            row--;
            column++;
        }
        System.out.println(row+", "+column);
        return diagonal;
    }

    private Position getStartPositionUp(Position pos){
        int row = pos.row;
        int column = pos.column;

        while(row>0 && column >0){
            row--;
            column--;
        }
        return new Position(row, column);
    }
    private Position getStartPositionDown(Position pos){
        int row = pos.row;
        int column = pos.column;

        while(row<(degree-1) && column>0){
            row++;
            column--;
        }
        return new Position(row, column);
    }

    public static void main(String[] args) {
        ResultMatrix resultMatrix = new ResultMatrix(5);
        resultMatrix.values[0][0] = Sign.CROSS;
        resultMatrix.values[1][1] = Sign.CROSS;
        resultMatrix.values[2][2] = Sign.CROSS;


        Position pos = new Position(4,0);
        System.out.println();
        List<Sign> row = resultMatrix.findGrowingDiagonal(pos);
        System.out.println(row);


    }

}
