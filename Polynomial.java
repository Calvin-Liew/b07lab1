public class Polynomial {
    double[] coefficients;

    public Polynomial(){
        this.coefficients = new double[0];
    }

    public Polynomial(double[] coefficients){
        this.coefficients = coefficients;
    }

    public Polynomial add(Polynomial polynomialToAdd){
        double[] polynomialCo1 = this.coefficients;
        double[] polynomialCo2 = polynomialToAdd.coefficients;

        int polynomialCo1len = polynomialCo1.length;
        int polynomialCo2len = polynomialCo2.length;

        int length = Math.max(polynomialCo1len, polynomialCo2len);
        double[] added = new double[length];

        int index = 0;

        while (index < length){
            if(index < polynomialCo1len && index < polynomialCo2len){
                added[index] = polynomialCo1[index] + polynomialCo2[index];
            } else if (index >= polynomialCo1len) {
                added[index] = polynomialCo2[index];
            } else{
                added[index] = polynomialCo1[index];
            }
            index++;
        }
        return new Polynomial(added);
    }

    public Double evaluate(double x){
        double[] coefficients = this.coefficients;
        double result = 0;
        for(int i = 0; i < coefficients.length; i++){
            result += coefficients[i]*Math.pow(x, i);
        }
        return result;
    }

    public boolean hasRoot(double x){
        return evaluate(x) == 0;
    }


}
