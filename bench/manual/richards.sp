
; Based on V8 JavaScript richards

ID_IDLE:       0,
ID_WORKER:     1,
ID_HANDLER_A:  2,
ID_HANDLER_B:  3,
ID_DEVICE_A:   4,
ID_DEVICE_B:   5,
NUMBER_OF_IDS: 6,

COUNT:         10000,

EXPECTED_QUEUE_COUNT: 23246,
EXPECTED_HOLD_COUNT: 9297,

STATE_RUNNING:   0,
STATE_RUNNABLE:  1,
STATE_SUSPENDED: 2,
STATE_HELD:      4,
STATE_SUSPENDED_RUNNABLE: STATE_SUSPENDED | STATE_RUNNABLE,
STATE_NOT_HELD: -5,

DATA_SIZE: 4,

; The Richards benchmark simulates the task dispatcher of an
; operating system.
runRichards = #(
  scheduler = Scheduler create
  queue = Packet create(nil, ID_WORKER, :work)
  workerQueue = Packet create(queue, ID_WORKER, :work)
  
  queue = Packet create(nil,   ID_DEVICE_A, :device)
  queue = Packet create(queue, ID_DEVICE_A, :device)
  handlerQueue = Packet create(queue, ID_DEVICE_A, :device)
  
  queue = Packet create(nil,   ID_DEVICE_B, :device)
  queue = Packet create(queue, ID_DEVICE_B, :device)
  handlerQueue2 = Packet create(queue, ID_DEVICE_B, :device)
  
  s0 = scheduler withIdleTask(ID_IDLE, 0, nil, COUNT)
  s1 = s0        withWorkerTask(ID_WORKER, 1000, workerQueue) 
  s2 = s1        withHandlerTask(ID_HANDLER_A, 2000, handlerQueue) 
  s3 = s2        withHandlerTask(ID_HANDLER_B, 3000, handlerQueue2)
  s4 = s3        withDeviceTask(ID_DEVICE_A, 4000, nil)
  s5 = s4        withDeviceTask(ID_DEVICE_B, 5000, nil)
  
  result = s5 schedule!

  if(result queueCount != EXPECTED_QUEUE_COUNT || result holdCount != EXPECTED_HOLD_COUNT,
    "Error during execution: queueCount = #{result  queueCount}, holdCount = #{result holdCount}." println)
)

