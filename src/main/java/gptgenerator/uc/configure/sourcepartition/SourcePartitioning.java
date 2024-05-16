package gptgenerator.uc.configure.sourcepartition;

import java.util.ArrayList;
import java.util.List;

import gptgenerator.uc.configure.merge.ITemplateConfigModel;

public class SourcePartitioning {
	private List<ISourcePartitionModel> sourcePartitions = new ArrayList<>();
	
	public SourcePartitioning() {
		
	}

	public void setPartitions(List<ISourcePartitionModel> partitions) {
		sourcePartitions = partitions;		
	}
	
	public void setPartition(int index, ISourcePartitionModel partition) {
		sourcePartitions.set(index, partition);		
	}

	public void remove(int deleteIndex) {
		sourcePartitions.remove(deleteIndex);		
	}

	public void addPartition(ISourcePartitionModel partition) {
		sourcePartitions.add(partition);		
	}

	public ISourcePartitionModel getPartition(int index) {
		return sourcePartitions.get(index);
	}

	public List<ISourcePartitionModel> getPartitions() {
		return sourcePartitions;		
	}
	
	public String mapToDestination(String sourceRelFilename) {
		for (ISourcePartitionModel cur: sourcePartitions) {
			if (cur.isHomeOf(sourceRelFilename)) {
				return cur.mapToDestination(sourceRelFilename);
			}
		}
		return null;
	}

	public String getDestDirAbs(String sourceRelFilename) {
		for (ISourcePartitionModel cur: sourcePartitions) {
			if (cur.isHomeOf(sourceRelFilename)) {
				return cur.getDestDirAbs();
			}
		}
		System.err.println("getDestDirAbs("+sourceRelFilename+") null pointer");
		return null;
	}
	
	public String getSystemMessage(String sourceRelFilename) {
		for (ISourcePartitionModel cur: sourcePartitions) {
			if (cur.isHomeOf(sourceRelFilename)) {
				return cur.getGptConfig().getSystemMessage();
			}
		}
		return null;
	}

	public Double getIndividualTemperature(String sourceRelFilename) {
		for (ISourcePartitionModel cur: sourcePartitions) {
			if (cur.isHomeOf(sourceRelFilename)) {
				return cur.getGptConfig().getTemperature();
			}
		}
		return null;
	}

	public boolean hasIndividualTemperature(String sourceRelFilename) {
		for (ISourcePartitionModel cur: sourcePartitions) {
			if (cur.isHomeOf(sourceRelFilename)) {
				return cur.getGptConfig().getIndividualTemperature();
			}
		}
		return false;
	}

	public ITemplateConfigModel getTemplateConfig(String sourceRelFilename) {
		for (ISourcePartitionModel cur: sourcePartitions) {
			if (cur.isHomeOf(sourceRelFilename)) {
				return cur.getTemplateConfig();
			}
		}
		return null;
	}
		
}
