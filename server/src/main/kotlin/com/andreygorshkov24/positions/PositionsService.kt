package com.andreygorshkov24.positions

import com.andreygorshkov24.Child

abstract class PositionsService<C : Child> {

  protected fun targetUp(targetId: String): Result {
    val target = findTarget(targetId) ?: return Result.CHILD_NOT_FOUND

    val targetRelatives = findTargetRelatives(target.parentId).filter { targetRelative -> targetRelative.id != target.id }

    if (target.position <= targetRelatives.size - 1) {
      val targetRelative = targetRelatives[targetRelatives.size - target.position - 1]
      targetRelative.position--

      target.position++
    }

    return Result.SUCCEEDED
  }

  protected fun targetDown(id: String): Result {
    val target = findTarget(id) ?: return Result.CHILD_NOT_FOUND

    val targetRelatives = findTargetRelatives(target.parentId).filter { targetRelative -> targetRelative.id != target.id }

    if (target.position > 0) {
      val targetRelative = targetRelatives[targetRelatives.size - target.position]
      targetRelative.position++

      target.position--
    }

    return Result.SUCCEEDED
  }

  abstract fun findTarget(targetId: String): C?

  /**
   * @param parentId target parent id
   */
  abstract fun findTargetRelatives(parentId: String?): Iterable<C>
}

enum class Result {
  CHILD_NOT_FOUND, SUCCEEDED
}
