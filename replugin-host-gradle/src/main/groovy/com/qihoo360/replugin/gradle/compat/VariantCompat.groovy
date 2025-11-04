package com.qihoo360.replugin.gradle.compat

import org.gradle.api.Task

/**
 * @author hyongbai
 */
class VariantCompat {

    static def getAssembleTask(def variant) {
        return compatGetTask(variant, 'getAssembleProvider', 'getAssemble')
    }

    static def getMergeAssetsTask(def variant) {
        return compatGetTask(variant, 'getMergeAssetsProvider', 'getMergeAssets')
    }

    static def getGenerateBuildConfigTask(def variant) {
        return compatGetTask(variant, 'getGenerateBuildConfigProvider', 'getGenerateBuildConfig')
    }

    static def getProcessManifestTask(def variant) {
        return compatGetTask(variant, 'getProcessManifestProvider', 'getProcessManifest')
    }

    static def compatGetTask(def variant, String... candidates) {
        candidates?.findResult { methodName ->
            variant.metaClass.respondsTo(variant, methodName).with {
                if (!it.isEmpty()) return it
            }
        }?.find {
            it.getParameterTypes().length == 0
        }?.invoke(variant)?.with { result ->
            // 检查是否是Provider类型
            if (result != null &&
                (result.getClass().simpleName.contains('Provider') ||
                result.getClass().name.contains('Provider'))) {
                // 如果是Provider，则调用get()方法
                return result.get()
            } else if (result instanceof Task) {
                // 直接是Task对象
                return result
            } else {
                // 其他情况返回原始结果
                return result
                }
        }
    }

}
