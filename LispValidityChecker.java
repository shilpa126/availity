package checker;

import java.util.Scanner;
import java.util.Stack;

public class LispValidityChecker {

	boolean checkvalidity(String code) {
		
		Stack<Character> stack = new Stack<>();
		for (char c : code.toCharArray()) {
			if (c=='(' || c == ')') {
				if (c == '(') {
					stack.push(c);
				} else if (!stack.empty() && ')' == c)
					stack.pop();
				else
					return false;
			}

		}

		return stack.empty();
	}

	public static void main(String args[]) {

		LispValidityChecker obj = new LispValidityChecker();
		Scanner s = new Scanner(System.in);
		System.out.print("Please enter code");
		System.out.println("Are all parantheses closed :" + obj.checkvalidity(s.nextLine()));
		s.close();
	}
}
