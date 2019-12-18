package com.baizhi.entity

/**
 * 用户评估状态
 */
class EvalState extends Serializable {

  var historyData: HistoryData = _
  var dayTime: String = _

}

object EvalState {
  def apply(): EvalState = new EvalState()
}
