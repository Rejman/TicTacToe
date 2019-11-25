package Models.Game;

import java.util.LinkedList;
import java.util.List;

public class ResultMatrix implements Cloneable{
    public int degree;
    private Sign[][] values;

    public String getHash(){
        String result = "";
        for(int i=0;i<degree;i++){
            for(int j=0;j<degree;j++){
                switch (values[i][j]){
                    case CIRCLE:
                        result+="O";
                        break;
                    case CROSS:
                        result+="X";
                        break;
                    case NONE:
                        result+="-";
                }

            }
        }
        return result;
    }
    public ResultMatrix clone(){
        ResultMatrix clone = new ResultMatrix(this.degree);
        Sign[][] copyOfValue = getCopyValues();
        clone.setValues(copyOfValue);

        return clone;
    }
    private Sign[][] getCopyValues(){
        Sign[][] copy = new Sign[degree][degree];
        for(int i=0;i<degree;i++){
            for(int j=0;j<degree;j++){
                copy[i][j] = values[i][j];
            }
        }
        return copy;
    }
    public void mirror(){
        ResultMatrix newMatrix = new ResultMatrix(degree);
        int index = degree;
        for(int i=0;i<degree;i++){
            index--;
            newMatrix.setColumn(this.getColumn(i),index);
        }
        this.setValues(newMatrix.getValues());
    }
    public void rule(){
        ResultMatrix newMatrix = new ResultMatrix(degree);
        for(int i=degree-1;i>=0;i--){
            newMatrix.setColumn(this.getRevertRow(i),i);
        }
        this.setValues(newMatrix.getValues());
    }
    public int getDegree() {
        return degree;
    }

    public Sign[][] getValues() {
        return values;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void setValues(Sign[][] values) {
        this.values = values;
    }

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
    public Sign[] getRevertRow(int number){
        Sign[] column = values[number];
        Sign[] revertRow = new Sign[degree];
        int index = degree;
        for(int i=0;i<degree;i++){
            index--;
            revertRow[i] = column[index];

        }
        return revertRow;
    }
    public void setRow(Sign[] row, int number){
        values[number] = row;
    }
    public Sign[] getColumn(int number){
        Sign[] column = new Sign[degree];
        for(int i=0;i<degree;i++){
            column[i] = values[i][number];
        }
        return column;
    }
    public void setColumn(Sign[] column, int number){
        for(int i=0;i<degree;i++){
            values[i][number] = column[i];
        }
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
    public List findFallingDiagonal(int field){
        Position pos = Position.convertToPositon(field,degree);
        return findFallingDiagonal(pos);
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
    public List findGrowingDiagonal(int field){
        Position pos = Position.convertToPositon(field,degree);
        return findGrowingDiagonal(pos);
    }
    public List findGrowingDiagonal(Position pos){
        Position start = getStartPositionDown(pos);
        List<Sign> diagonal = new LinkedList<Sign>();
        int row=start.row;
        int column=start.column;

        while (row>=0 && column<degree){
            diagonal.add(values[row][column]);
            row--;
            column++;
        }
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
    public void add(Position position, Sign sign){
        values[position.row][position.column] = sign;
    }
    public void add(int field, Sign sign){
        Position pos = Position.convertToPositon(field, degree);
        add(pos,sign);
    }

    public static void main(String[] args) {
        ResultMatrix resultMatrix = new ResultMatrix(3);
        /*resultMatrix.values[0][0] = Sign.CROSS;
        resultMatrix.values[1][1] = Sign.CROSS;
        resultMatrix.values[2][2] = Sign.CIRCLE;
        resultMatrix.values[1][2] = Sign.CIRCLE;*/

        resultMatrix.values[0][0] = Sign.CIRCLE;
        resultMatrix.values[0][1] = Sign.CIRCLE;
        resultMatrix.values[0][2] = Sign.CROSS;


        //Position pos = new Position(4,0);
        //List<Sign> row = resultMatrix.findGrowingDiagonal(pos);


        resultMatrix.showIn3D();
        resultMatrix.mirror();
        resultMatrix.showIn3D();



    }
    private void showIn3D(){
        String hash = this.getHash();
        System.out.println(hash.substring(0,3));
        System.out.println(hash.substring(3,6));
        System.out.println(hash.substring(6));
    }

}
