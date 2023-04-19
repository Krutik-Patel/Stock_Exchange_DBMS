package DAO;
import Table.*;
// import java.lang.*;
// import java.util.List;

public interface ParticipantDAO {
	public Participants getParticipantByKey(int regs_id);
	public void addParticipant(Participants participant) throws Exception;
	public void updateParticipant(Participants participants) throws Exception;
	public void deleteParticipant(Participants participant);
	
}

