import javax.swing.JOptionPane;
public class Game {
    public int masPlay[][];
    public int masComp[][];
    public static int endGame=3;
    public int P1,P2,P3,P4;
    public int C1,C2,C3,C4;
    public int kolHodPlay;
    public int kolHodComp;
    public static boolean gamePkVsPk;
    public final int pause=500;
    public boolean myHod;
    public boolean compHod;
    Thread thread=new Thread();
    Game() {
        masPlay = new int[10][10];
        masComp = new int[10][10];
    }
    public void start() {
        while (thread.isAlive())gamePkVsPk = false;
        kolHodComp=0;
        kolHodPlay=0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                masPlay[i][j] = 0;
                masComp[i][j] = 0;
            }
        }
        gamePkVsPk=false;
        myHod =true;
        compHod=false;
        endGame=0;
        kolUbitPk(masComp);
        kolUbitPlayer(masPlay);
        if (Panel.rasstanovka==false) {
            setPalubaPlay();
        }
        setPalubaComp();
    }
    public void attack(int mas[][],int i,int j) {
        kolHodPlay++;
        mas[i][j] += 7;
        testUbit(mas, i, j);
        testEndGame();
        thread =new Thread(new Runnable() {
            @Override
            public void run() {
                //если промах
                if (masComp[i][j] < 8) {
                    myHod = false;
                    compHod = true;
                    while (compHod == true) {
                        try {
                            Thread.sleep(pause);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        compHod = compHodit(masPlay);
                    }
                    myHod = true;
                }
            }
        });
        thread.start();
    }
    public void autoGame() {
        thread =new Thread(new Runnable() {
            @Override
            public void run() {
                kolHodComp=0;
                kolHodPlay=0;
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        masPlay[i][j] = 0;
                        masComp[i][j] = 0;
                    }
                }
                myHod =false;
                compHod=true;
                endGame=0;
                gamePkVsPk=true;
                kolUbitPk(masComp);
                kolUbitPlayer(masPlay);
                setPalubaPlay();
                setPalubaComp();
                while (endGame == 0 && gamePkVsPk) {
                    kolUbitPk(masComp);
                    kolUbitPlayer(masPlay);
                    myHod = true;
                    while (endGame == 0 && myHod  && gamePkVsPk) {
                        try {Thread.sleep(pause);} catch (InterruptedException e) {e.printStackTrace();}
                        kolHodPlay++;
                        myHod = compHodit(masComp);
                    }
                    compHod = true;
                    while (endGame == 0 && compHod == true && gamePkVsPk) {
                        try {Thread.sleep(pause);} catch (InterruptedException e) {e.printStackTrace();}
                        kolHodComp++;
                        compHod = compHodit(masPlay);
                    }
                }
            }
        });
        thread.start();
    }

    public void testEndGame(){
        if (endGame==0){
            int sumEnd=330;
            int sumPlay=0;
            int sumComp=0;
            kolUbitPk(masComp);
            kolUbitPlayer(masPlay);
            if (endGame==0) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (masPlay[i][j] >= 15) sumPlay += masPlay[i][j];
                        if (masComp[i][j] >= 15) sumComp += masComp[i][j];
                    }
                }
                if (sumPlay == sumEnd) {
                    endGame = 2;
                    JOptionPane.showMessageDialog(null,
                            "Ти програв, але не здавайся.",
                            "Поразка", JOptionPane.INFORMATION_MESSAGE);

                } else if (sumComp == sumEnd) {
                    endGame = 1;
                    JOptionPane.showMessageDialog(null,
                            "Ти виграв битву, але не війну.",
                            "Перемога", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    public void kolUbitPk(int[][]mas){
        P4=0;P3=0;P2=0;P1=0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (mas[i][j]==18) P4 = (P4 + 1);
                if (mas[i][j]==17) P3 = (P3 + 1);
                if (mas[i][j]==16) P2 = (P2 + 1);
                if (mas[i][j]==15) P1 = (P1 + 1);
            }
        }
        P4/=4;P3/=3;P2/=2;
    }
    public void kolUbitPlayer(int[][]mas) {
        C4 = 0;C3 = 0;C2 = 0;C1 = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (mas[i][j] == 18) C4 = (C4 + 1);
                if (mas[i][j] == 17) C3 = (C3 + 1);
                if (mas[i][j] == 16) C2 = (C2 + 1);
                if (mas[i][j] == 15) C1 = (C1 + 1);
            }
        }
        C4/=4;C3/=3;C2/=2;
    }
    private void testUbit(int mas[][],int i,int j){
        if (mas[i][j]==8) {
            mas[i][j] += 7;
            setOkrUbit(mas,i,j);
        }
        else if (mas[i][j]==9){
            analizUbit(mas,i,j,2);
        }
        else if (mas[i][j]==10){
            analizUbit(mas,i,j,3);
        }
        else if (mas[i][j]==11){
            analizUbit(mas,i,j,4);
        }
    }
    private void analizUbit(int[][] mas, int i, int j, int kolPalub) {
        int kolRanen=0;
        for (int x=i-(kolPalub-1);x<=i+(kolPalub-1);x++) {
            for (int y=j-(kolPalub-1);y<=j+(kolPalub-1);y++) {
                if (testMasOut(x, y)&&(mas[x][y]==kolPalub+7)) kolRanen++;
            }
        }
        if (kolRanen==kolPalub) {
            for (int x=i-(kolPalub-1);x<=i+(kolPalub-1);x++) {
                for (int y=j-(kolPalub-1);y<=j+(kolPalub-1);y++) {
                    if (testMasOut(x, y)&&(mas[x][y]==kolPalub+7)) {
                        mas[x][y]+=7;
                        setOkrUbit(mas, x, y);
                    }
                }
            }
        }
    }



    public void setOkrKilled(int mas[][],int i,int j){
        if (testMasOut(i, j)){
            if (mas[i][j]==-1 || mas[i][j]==6){
                mas[i][j]--;
            }
        }
    }
    private void setOkrUbit(int[][] mas, int i, int j) {
        setOkrKilled(mas, i - 1, j - 1);
        setOkrKilled(mas, i - 1, j);
        setOkrKilled(mas, i - 1, j + 1);
        setOkrKilled(mas, i, j + 1);
        setOkrKilled(mas, i + 1, j + 1);
        setOkrKilled(mas, i + 1, j);
        setOkrKilled(mas, i + 1, j - 1);
        setOkrKilled(mas, i, j - 1);
    }
    boolean compHodit(int mas[][]) {
        if ((endGame == 0 && gamePkVsPk) || (compHod && !gamePkVsPk)) {
            if (gamePkVsPk == false) kolHodComp++;
            boolean popal = false;
            boolean ranen = false;
            boolean horiz = false;
            _for1:
            for (int i = 0; i < 10; i++)
                for (int j = 0; j < 10; j++)
                    if ((mas[i][j] >= 9) && (mas[i][j] <= 11)) {
                        ranen = true;
                        if ((testMasOut(i - 3, j) && mas[i - 3][j] >= 9 && (mas[i - 3][j] <= 11))
                                || (testMasOut(i - 2, j) && mas[i - 2][j] >= 9 && (mas[i - 2][j] <= 11))
                                || (testMasOut(i - 1, j) && mas[i - 1][j] >= 9 && (mas[i - 1][j] <= 11))
                                || (testMasOut(i + 3, j) && mas[i + 3][j] >= 9 && (mas[i + 3][j] <= 11))
                                || (testMasOut(i + 2, j) && mas[i + 2][j] >= 9 && (mas[i + 2][j] <= 11))
                                || (testMasOut(i + 1, j) && mas[i + 1][j] >= 9 && (mas[i + 1][j] <= 11))) {
                            horiz = true;
                        } else if ((testMasOut(i, j + 3) && mas[i][j + 3] >= 9 && (mas[i][j + 3] <= 11))
                                || (testMasOut(i, j + 2) && mas[i][j + 2] >= 9 && (mas[i][j + 2] <= 11))
                                || (testMasOut(i, j + 1) && mas[i][j + 1] >= 9 && (mas[i][j + 1] <= 11))
                                || (testMasOut(i, j - 3) && mas[i][j - 3] >= 9 && (mas[i][j - 3] <= 11))
                                || (testMasOut(i, j - 2) && mas[i][j - 2] >= 9 && (mas[i][j - 2] <= 11))
                                || (testMasOut(i, j - 1) && mas[i][j - 1] >= 9 && (mas[i][j - 1] <= 11))) {
                            horiz = false;
                        }
                        else for (int x = 0; x < 50; x++) {
                                int napr = (int) (Math.random() * 4);
                                if (napr == 0 && testMasOut(i - 1, j) && (mas[i - 1][j] <= 4) && (mas[i - 1][j] != -2)) {
                                    mas[i - 1][j] += 7;
                                    testUbit(mas, i - 1, j);
                                    if (mas[i - 1][j] >= 8) popal = true;
                                    break _for1;
                                } else if (napr == 1 && testMasOut(i + 1, j) && (mas[i + 1][j] <= 4) && (mas[i + 1][j] != -2)) {
                                    mas[i + 1][j] += 7;
                                    testUbit(mas, i + 1, j);
                                    if (mas[i + 1][j] >= 8) popal = true;
                                    break _for1;
                                } else if (napr == 2 && testMasOut(i, j - 1) && (mas[i][j - 1] <= 4) && (mas[i][j - 1] != -2)) {
                                    mas[i][j - 1] += 7;
                                    testUbit(mas, i, j - 1);
                                    if (mas[i][j - 1] >= 8) popal = true;
                                    break _for1;
                                } else if (napr == 3 && testMasOut(i, j + 1) && (mas[i][j + 1] <= 4) && (mas[i][j + 1] != -2)) {
                                    mas[i][j + 1] += 7;
                                    testUbit(mas, i, j + 1);
                                    if (mas[i][j + 1] >= 8) popal = true;
                                    break _for1;
                                }
                            }
                        if (horiz) {
                            if (testMasOut(i - 1, j) && (mas[i - 1][j] <= 4) && (mas[i - 1][j] != -2)) {
                                mas[i - 1][j] += 7;
                                testUbit(mas, i - 1, j);
                                if (mas[i - 1][j] >= 8) popal = true;
                                break _for1;
                            } else if (testMasOut(i + 1, j) && (mas[i + 1][j] <= 4) && (mas[i + 1][j] != -2)) {
                                mas[i + 1][j] += 7;
                                testUbit(mas, i + 1, j);
                                if (mas[i + 1][j] >= 8) popal = true;
                                break _for1;
                            }
                        }
                        else if (testMasOut(i, j - 1) && (mas[i][j - 1] <= 4) && (mas[i][j - 1] != -2)) {
                            mas[i][j - 1] += 7;
                            testUbit(mas, i, j - 1);
                            if (mas[i][j - 1] >= 8) popal = true;
                            break _for1;
                        } else if (testMasOut(i, j + 1) && (mas[i][j + 1] <= 4) && (mas[i][j + 1] != -2)) {
                            mas[i][j + 1] += 7;
                            testUbit(mas, i, j + 1);
                            if (mas[i][j + 1] >= 8) popal = true;
                            break _for1;
                        }
                    }

            if (ranen == false) {
                for (int l = 1; l <= 100; l++) {
                    int i = (int) (Math.random() * 10);
                    int j = (int) (Math.random() * 10);
                    if ((mas[i][j] <= 4) && (mas[i][j] != -2)) {
                        mas[i][j] += 7;
                        testUbit(mas, i, j);
                        if (mas[i][j] >= 8)
                            popal = true;
                        ranen = true;
                        break;
                    }
                }
            }
            testEndGame();
            return popal;
        }else return false;
    }
    private boolean testMasOut(int i, int j) {
        if (((i >= 0) && (i <= 9)) && ((j >= 0) && (j <= 9))) {
            return true;
        } else return false;
    }

    private void setOkr(int[][] mas, int i, int j, int val) {
        if (testMasOut(i, j) && mas[i][j] == 0) {
            mas[i][j] = val;
        }
    }

    private void okrBegin(int[][] mas, int i, int j, int val) {
        setOkr(mas, i - 1, j - 1, val);
        setOkr(mas, i - 1, j, val);
        setOkr(mas, i - 1, j + 1, val);
        setOkr(mas, i, j + 1, val);
        setOkr(mas, i, j - 1, val);
        setOkr(mas, i + 1, j + 1, val);
        setOkr(mas, i + 1, j, val);
        setOkr(mas, i + 1, j - 1, val);
    }
    public boolean setPaluba(int i, int j, int kolPal,boolean napr){
        boolean flag = false;
        if (testNewPaluba(masPlay, i, j) == true) {
            if (napr==false){
                if (testNewPaluba(masPlay, i, j + (kolPal - 1)) == true)
                    flag = true;
            }
            else if (napr){
                if (testNewPaluba(masPlay, i + (kolPal - 1), j) == true)
                    flag = true;
            }
        }
        if (flag == true) {
            masPlay[i][j] = kolPal;
            okrBegin(masPlay, i, j, -2);
            if (napr==false){
                for (int k = kolPal - 1; k >= 1; k--) {
                    masPlay[i][j + k] = kolPal;
                    okrBegin(masPlay, i, j + k, -2);
                }
            }
            else if (napr){
                for (int k = kolPal - 1; k >= 1; k--) {
                    masPlay[i + k][j] = kolPal;
                    okrBegin(masPlay, i + k, j, -2);
                }
            }
        }
        okrEnd(masPlay);
        return flag;
    }

    private boolean testNewPaluba(int [][]mas,int i, int j){
        if (testMasOut(i, j)==false) return false;
        if ((mas[i][j]==0) || (mas[i][j]==-2)) return true;
        else return false;
    }

    private void okrEnd(int[][] mas) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (mas[i][j] == -2)
                    mas[i][j] = -1;
            }
        }
    }


    private void setPalubaAuto(int [][]mas, int kolPal){
        int i = 0, j = 0;
        while (true) {
            boolean flag = false;
            i = (int) (Math.random() * 10);
            j = (int) (Math.random() * 10);
            int napr = (int) (Math.random() * 4);

            if (testNewPaluba(mas, i, j) == true) {
                if (napr == 0) {
                    if (testNewPaluba(mas, i -(kolPal - 1), j) == true)
                        flag = true;
                }
                else if (napr == 1){
                    if (testNewPaluba(mas, i, j + (kolPal - 1)) == true)
                        flag = true;
                }
                else if (napr == 2){
                    if (testNewPaluba(mas, i + (kolPal - 1), j) == true)
                        flag = true;
                }
                else if (napr == 3){
                    if (testNewPaluba(mas, i, j -(kolPal - 1)) == true)
                        flag = true;
                }
            }
            if (flag == true) {
                mas[i][j] = kolPal;
                okrBegin(mas, i, j, -2);
                if (napr == 0) {
                    for (int k = kolPal - 1; k >= 1; k--) {
                        mas[i -k][j] = kolPal;
                        okrBegin(mas, i - k, j, -2);
                    }
                }
                else if (napr == 1){
                    for (int k = kolPal - 1; k >= 1; k--) {
                        mas[i][j + k] = kolPal;
                        okrBegin(mas, i, j + k, -2);
                    }
                }
                else if (napr == 2){
                    for (int k = kolPal - 1; k >= 1; k--) {
                        mas[i + k][j] = kolPal;
                        okrBegin(mas, i + k, j, -2);
                    }
                }
                else if (napr == 3){
                    for (int k = kolPal - 1; k >= 1; k--) {
                        mas[i][j -k] = kolPal;
                        okrBegin(mas, i, j - k, -2);
                    }
                }
                break;
            }
        }
        okrEnd(mas);
    }
    private void setPalubaPlay(){
        setPalubaAuto(masPlay, 4);
        for (int i = 1; i <= 2; i++) {
            setPalubaAuto(masPlay, 3);
        }
        for (int i = 1; i <= 3; i++) {
            setPalubaAuto(masPlay, 2);
        }
        for (int i = 1;i<= 4;i++){
            setPalubaAuto(masPlay,1);
        }
    }
    private void setPalubaComp(){
        setPalubaAuto(masComp, 4);
        for (int i = 1; i <= 2; i++) {
            setPalubaAuto(masComp, 3);
        }
        for (int i = 1; i <= 3; i++) {
            setPalubaAuto(masComp, 2);
        }
        for (int i = 1;i<= 4;i++){
            setPalubaAuto(masComp,1);
        }
    }
}
