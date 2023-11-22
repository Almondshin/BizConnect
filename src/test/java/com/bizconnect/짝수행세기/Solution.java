package src.test.java.com.bizconnect.짝수행세기;

/**
 * main, PRINT_RESULT 는 테스트 케이스 실행 및 결과 확인을 위한 함수입니다.
 * [답안지 복사] 기능을 사용하는 경우 해당 함수들을 제외하며, 답안에 필요한 코드만 복사됩니다.
 * 테스트 케이스 추가 등 함수 내부 변경은 가능하나, 함수 이름 변경시 [답안지 복사] 기능이 제대로 동작하지 않습니다.
 *
 * 또한, 기본 설정으로 [답안지 복사] 사용시 해당 주석과 작성하신 주석을 제외하여 복사됩니다.
 * [주석 복사] 여부는 설정을 통해 변경할 수 있습니다.
 *
 * [도움말 주석] 옵션은 설정을 통해 제거할 수 있습니다.
 *
 * - [답안지 복사]
 *   코드 - 답안지 복사 (기본 단축키 cmd + shift + w)
 *
 * - [도움말 주석]
 *   설정 - 도구 - 프로그래머스 헬퍼 - 도움말 주석
 *
 * - [주석 복사]
 *   설정 - 도구 - 프로그래머스 헬퍼 - 주석 복사
 *
 * GitHub: https://github.com/azqazq195/programmers_helper
 */
class Solution {
    public static void main(String[] args) {
        int[][] a1 = new int[][]{{0, 1, 0}, {1, 1, 1}, {1, 1, 0}, {0, 1, 1}};
        int answer1 = 6;
        int result1 = new Solution().solution(a1);
        PRINT_RESULT(1, result1, answer1);

        int[][] a2 = new int[][]{{1, 0, 0}, {1, 0, 0}};
        int answer2 = 0;
        int result2 = new Solution().solution(a2);
        PRINT_RESULT(2, result2, answer2);

        int[][] a3 = new int[][]{{1, 0, 0, 1, 1}, {0, 0, 0, 0, 0}, {1, 1, 0, 0, 0}, {0, 0, 0, 0, 1}};
        int answer3 = 72;
        int result3 = new Solution().solution(a3);
        PRINT_RESULT(3, result3, answer3);
    }

    public static void PRINT_RESULT(int index, int result, int answer) {
        boolean correct = result == answer;
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n테스트 케이스 ").append(index).append(": ");
        sb.append(correct ? "정답" : "오답").append("\n");
        sb.append("\t- 실행 결과: \t").append(result).append("\n");
        sb.append("\t- 기댓값: \t").append(answer).append("\n");
        if (correct) System.out.println(sb);
        else throw new RuntimeException(sb.toString());
    }


    /*
    * 모든 수가 0 또는 1로 이루어진 2차원 배열 a가 주어집니다.
    * 다음 조건을 모두 만족하는 2차원 배열 b의 경우의 수를 (107 + 19)로 나눈 나머지를 return 하도록 solution 함수를 완성해주세요.
    * b의 모든 원소는 0 아니면 1입니다.
    * a의 행/열의 개수와 b의 행/열의 개수가 같습니다. (= a와 b의 크기가 같습니다.)
    * i = 1, 2, ..., (a의 열의 개수)에 대해서 a의 i번째 열과 b의 i번째 열에 들어 있는 1의 개수가 같습니다.
    * b의 각 행에 들어 있는 1의 개수가 짝수입니다. (0도 짝수입니다.)
    * a의 행의 개수는 1 이상 300 이하입니다.
    * a의 각 행의 길이는 1 이상 300 이하로 모두 동일합니다.
    * */
    public int solution(int[][] a) {
        int answer = -1;




        return answer % (10000000 + 19);
    }
}