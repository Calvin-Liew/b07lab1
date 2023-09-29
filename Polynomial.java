import java.io.*;

public class Polynomial {
    double[] coefficients;
    int[] exponents;

    private int indexOfArray(int[] array, int item){
        for(int i = 0; i < array.length; i++){
            if(array[i] == item){
                return i;
            }
        }
        return -1;
    }

    private int indexOfArray(String[] array, String item){
        for(int i = 0; i < array.length; i++){
            if(array[i].equals(item)){
                return i;
            }
        }
        return -1;
    }

    private boolean isInList(int[] array, int item){
        for (int j : array) {
            if (j == item) {
                return true;
            }
        }
        return false;
    }

    private boolean isInList(String[] array, String item){
        for (String j : array) {
            if (j.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public Polynomial(){
        this.coefficients = new double[]{0};
        this.exponents = new int[]{0};
    }

    public Polynomial(double[] coefficients, int[] exponents){
        this.coefficients = coefficients;
        this.exponents = exponents;
    }

    public Polynomial add(Polynomial other) {
         int maxLength = other.exponents.length + this.exponents.length;

         for(int expo1 : this.exponents){
            for(int expo2 : other.exponents){
                if(expo1 == expo2){
                    maxLength -=1;
                }
            }
         }

         int[] newExpo = new int[maxLength];
         double[] newCoeff = new double[maxLength];
         int[] usedExpo = new int[maxLength];

         for(int i = 0; i < maxLength; i++){
            newExpo[i] = -1;
            newCoeff[i] = -1;
            usedExpo[i] = -1;
         }


         int newIndex = 0;

         for(int i = 0; i < other.exponents.length; i++){
             newExpo[newIndex] = other.exponents[i];
             newCoeff[newIndex] = other.coefficients[i];
             usedExpo[newIndex] = other.exponents[i];
             newIndex++;
         }

         for(int j = 0; j < this.exponents.length; j++){
            if(isInList(usedExpo, this.exponents[j])){
                newCoeff[indexOfArray(newExpo, this.exponents[j])] = newCoeff[indexOfArray(newExpo, this.exponents[j])]
                        + this.coefficients[j] ;
            } else{
                newExpo[newIndex] = this.exponents[j];
                newCoeff[newIndex] = this.coefficients[j];
                usedExpo[newIndex] = this.exponents[j];
                newIndex++;
            }
         }
         return new Polynomial(newCoeff, newExpo);
    }


    public double evaluate(double x){
        double total = 0;
        for(int i = 0; i < this.exponents.length; i++){
            total += this.coefficients[i]*Math.pow(x, this.exponents[i]);
        }
        return total;
    }

    public boolean hasRoot(double x){
        return evaluate(x) == 0;
    }

    private double getCoeff(String term){
        int start = 0;
        int end = 0;
        int index = 0;
        int modifier = 1;
        String[] splitTerm = term.split("");
        if(splitTerm[0] == "+" || !(splitTerm[0] == "-") && !(splitTerm[0] == "+")){
            modifier = 1;
        }else{
            modifier = -1;
        }
        if(!isInList(splitTerm, "x")){
            return Double.parseDouble(term.substring(index))*modifier;
        }else{
            while(term.charAt(index) != 'x'){
                end++;
                index++;
            }
            return Double.parseDouble(term.substring(start, end))*modifier;
        }

    }

    private int getExpo(String term){
        String[] terms = term.split("");
        if(!isInList(terms, "x")){
            return 0;
        }else{
            if(indexOfArray(terms, "x")+1 >= terms.length){
                return 1;
            }
            return Integer.parseInt(terms[indexOfArray(terms, "x")+1]);
        }
    }

    public Polynomial multiply(Polynomial p){

        int maxLength = p.exponents.length*this.exponents.length;
        int[] usedExpos = new int[maxLength];

        for(int i = 0; i < maxLength; i++){
            usedExpos[i] = -1;
        }


        int usedIndex = 0;
        for(int expo : p.exponents){
            for(int expo2 : this.exponents){
                if(isInList(usedExpos, expo+expo2)){
                    maxLength -= 1;
                }else{
                    usedExpos[usedIndex] = expo+expo2;
                    usedIndex++;
                }
            }
        }

        int[] newExpo = new int[maxLength];
        double[] newCoeff = new double[maxLength];
        for(int i = 0; i < maxLength; i++){
            newExpo[i] = -1;
            newCoeff[i] = -1;
        }

        int newIndex = 0;
        for(int i = 0; i < this.exponents.length; i++){
            for(int j = 0; j < p.exponents.length; j++){
                if(indexOfArray(newExpo, p.exponents[j]+this.exponents[i]) == -1){
                    newExpo[newIndex] = p.exponents[j]+this.exponents[i];
                    newCoeff[newIndex] = p.coefficients[j]*this.coefficients[i];
                    newIndex++;
                }else{
                    newCoeff[indexOfArray(newExpo, p.exponents[j]+this.exponents[i])]+=
                            p.coefficients[j]*this.coefficients[i];
                }
            }
        }

        return new Polynomial(newCoeff, newExpo);

    }

    public Polynomial(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        int numTerms = 1;

        for(int i = 1; i < line.length(); i++){
            if(line.charAt(i) == '+' || line.charAt(i) == '-'){
                numTerms++;
            }
        }

        int start = 0;
        int end;

        double[] coeffs = new double[numTerms];
        int[] expos = new int[numTerms];
        int termIndex = 0;

        for(int i = 0; i < line.length(); i++){
            char curr = line.charAt(i);

            if(curr == '+' || curr == '-'){
                if(i == 0 && curr == '-'){
                    continue;
                }
                end = i;
                coeffs[termIndex] = getCoeff(line.substring(start, end));
                expos[termIndex] = getExpo(line.substring(start, end));
                start = end;
                numTerms -= 1;
                termIndex++;
            }

            if(numTerms == 1){
                end = line.length();
                coeffs[termIndex] = getCoeff(line.substring(start, end));
                expos[termIndex] = getExpo(line.substring(start, end));
                start = end;
                numTerms -= 1;
            }
        }

        this.exponents = expos;
        this.coefficients = coeffs;
        bufferedReader.close();
        }

        public void saveToFile(String fileName) throws FileNotFoundException {
            PrintStream ps = new PrintStream(fileName);
            for(int i = 0; i < this.exponents.length; i++){
                if(this.coefficients[i] > 0){
                    if(i == 0){
                        ps.print(this.coefficients[i]);
                    }else{
                        ps.print("+"+this.coefficients[i]);
                    }
                }else{
                    ps.print(this.coefficients[i]);
                }
                if(this.exponents[i] >= 1){
                    ps.print("x"+this.exponents[i]);
                }

            }
            ps.close();
        }

    }




