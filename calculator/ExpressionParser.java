import java.util.Stack;

public class ExpressionParser {

    public Node parse(String expression) {
        Stack<Character> operators = new Stack<>();
        Stack<Node> operands = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                // Parse a number
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                operands.push(new Node(Double.parseDouble(sb.toString())));
                continue;
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    processOperator(operators, operands);
                }
                operators.pop(); // Remove '('
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    processOperator(operators, operands);
                }
                operators.push(ch);
            }

            i++;
        }

        while (!operators.isEmpty()) {
            processOperator(operators, operands);
        }

        return operands.pop();
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    private void processOperator(Stack<Character> operators, Stack<Node> operands) {
        char operator = operators.pop();
        Node right = operands.pop();
        Node left = operands.pop();
        operands.push(new Node(operator, left, right));
    }

    public static class Node {
        private char operator;
        private double value;
        private Node left, right;

        public Node(double value) {
            this.value = value;
        }

        public Node(char operator, Node left, Node right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }

        public double evaluate() {
            if (operator == 0) {
                return value;
            }
            switch (operator) {
                case '+':
                    return left.evaluate() + right.evaluate();
                case '-':
                    return left.evaluate() - right.evaluate();
                case '*':
                    return left.evaluate() * right.evaluate();
                case '/':
                    return left.evaluate() / right.evaluate();
                default:
                    throw new IllegalStateException("Unexpected operator: " + operator);
            }
        }
    }
}
