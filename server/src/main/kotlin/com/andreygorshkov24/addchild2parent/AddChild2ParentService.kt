package com.andreygorshkov24.addchild2parent

import com.andreygorshkov24.Child
import com.andreygorshkov24.Identifiable

abstract class AddChild2ParentService<C : Child, I : Identifiable> {

  protected fun addChild2Parent(childId: String, parentId: String): Result {
    findParent(parentId) ?: return Result.PARENT_NOT_FOUND

    val children = findChildren()

    val child = children.firstOrNull { it.id == childId } ?: return Result.CHILD_NOT_FOUND

    val oldParentId = child.parentId

    val position = calcPosition(children, parentId)

    child.parentId = parentId
    child.position = position

    recalcPosition(children.filter { it.parentId == oldParentId })
    return Result.SUCCEEDED
  }

  abstract fun findParent(parentId: String): I?

  abstract fun findChildren(): Iterable<C>

  private fun calcPosition(children: Iterable<Child>, parentId: String): Int {
    return children.count { child -> child.parentId == parentId }
  }

  private fun recalcPosition(children: Iterable<Child>) {
    var position = children.count()
    return children.forEach { child -> child.position = --position }
  }
}

enum class Result {
  PARENT_NOT_FOUND, CHILD_NOT_FOUND, SUCCEEDED
}
