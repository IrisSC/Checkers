package checkers;
/* Campares noes per sec, average branching factor, effective branching factor of the players
 * 
 */

public class Compare {
	public static void main(String[] args) {
		long minMaxNode = 0;
		double minMaxAve = 0;
		double minMaxEff = 0;
		MinMaxPlayer mm1 = new MinMaxPlayer(1);
		MinMaxPlayer mm2 = new MinMaxPlayer(1);
		
		long alphaBetaNode = 0;
		double alphaBetaAve = 0;
		double alphaBetaEff = 0;
		AlphaBetaPlayer ab1 = new AlphaBetaPlayer(1);
		AlphaBetaPlayer ab2 = new AlphaBetaPlayer(1);
		
		long nmpNode = 0;
		double nmpAve = 0;
		double nmpEff = 0;
		NullMovePruning nmp1 = new NullMovePruning(1);
		NullMovePruning nmp2 = new NullMovePruning(1);
		
		long startTimeMM = System.currentTimeMillis();
		for(int i = 0; i < 2; i++) {
			mm1 = new MinMaxPlayer(1);
			mm2 = new MinMaxPlayer(1);
			Checkers c = new Checkers(mm1, mm2, 1000);
			c.playGame();
			minMaxNode = minMaxNode + mm1.getNodesGenerated() + mm2.getNodesGenerated();
			minMaxAve = minMaxAve + mm1.getAveBranchingFactor() + mm2.getAveBranchingFactor();
			minMaxEff = minMaxEff + mm1.getEffectiveBranchingFactor() + mm2.getEffectiveBranchingFactor();
		}
		long endTimeMM = System.currentTimeMillis();
		
		
		long startTimeAB = System.currentTimeMillis();
		for(int i = 0; i < 2; i++) {
			ab1 = new AlphaBetaPlayer(1);
			ab2 = new AlphaBetaPlayer(1);
			Checkers c = new Checkers(ab1, ab2, 1000);
			c.playGame();
			alphaBetaNode = alphaBetaNode + ab1.getNodesGenerated() + ab2.getNodesGenerated();
			alphaBetaAve = alphaBetaAve + ab1.getAveBranchingFactor() + ab2.getAveBranchingFactor();
			alphaBetaEff = alphaBetaEff + ab1.getEffectiveBranchingFactor() + ab2.getEffectiveBranchingFactor();
		}
		long endTimeAB = System.currentTimeMillis();
		
		
		long startTimeNMP = System.currentTimeMillis();
		for(int i = 0; i < 2; i++) {
			nmp1 = new NullMovePruning(1);
			nmp2 = new NullMovePruning(1);
			Checkers c = new Checkers(nmp1, nmp2, 1000);
			c.playGame();
			nmpNode = nmpNode + nmp1.getNodesGenerated() + nmp2.getNodesGenerated();
			nmpAve = nmpAve + nmp1.getAveBranchingFactor() + nmp2.getAveBranchingFactor();
			nmpEff = nmpEff + nmp1.getEffectiveBranchingFactor() + nmp2.getEffectiveBranchingFactor();
		}
		long endTimeNMP = System.currentTimeMillis();
		
		
		long timeMM = endTimeMM - startTimeMM;
		long nodesPerSecMM = minMaxNode / (timeMM/1000);
		System.out.println ("nodes per second MM " + nodesPerSecMM);
		double aveMM = minMaxAve / 4;
		System.out.println("Average branching factor MinMax: " + aveMM);
		double effMM = minMaxEff/4;
		System.out.println("Effective branching factor MinMax: " + effMM);
		
		long timeAB = endTimeAB - startTimeAB;
		long nodesPerSecAB = alphaBetaNode / (timeAB/1000);
		System.out.println ("nodes per second AB " + nodesPerSecAB);
		double aveAB = alphaBetaAve / 4;
		System.out.println("Average branching factor AlphaBeta: " + aveAB);
		double effAB = alphaBetaEff/4;
		System.out.println("Effective branching factor AlphaBeta: " + effAB);
		
		long timeNMP = endTimeNMP - startTimeNMP;
		long nodesPerSecNMP = nmpNode / (timeNMP/1000);
		System.out.println ("nodes per second NMP " + nodesPerSecNMP);
		double aveNMP = nmpAve / 4;
		System.out.println("Average branching factor Null Move Pruning: " + aveNMP);
		double effNMP = nmpEff/4;
		System.out.println("Effective branching factor Null Move Pruning: " + effNMP);
		
	}
}
