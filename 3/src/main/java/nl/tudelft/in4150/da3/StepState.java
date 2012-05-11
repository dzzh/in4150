package nl.tudelft.in4150.da3;

public enum StepState {

	FORMS_POOL, //this step is accepting messages and didn't reach minimum number of them to proceed
	WAITS_FOR_TIME_OUT, //minimum number of messages reached but step waits for slow messages to come
	READY //step doesn't accept more messages and is ready for procession
	
}
