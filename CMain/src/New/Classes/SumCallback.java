package New.Classes;

import New.Classes.Project;

@FunctionalInterface
public interface SumCallback {
    void receiveSum(Project project, int sum);
}
