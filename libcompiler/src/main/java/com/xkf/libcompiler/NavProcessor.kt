package com.xkf.libcompiler

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.google.auto.service.AutoService
import com.xkf.libannotation.ActivityDestination
import com.xkf.libannotation.FragmentDestination
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation
import kotlin.math.abs

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = ["com.xkf.libannotation.FragmentDestination", "com.xkf.libannotation.ActivityDestination"])
class NavProcessor : AbstractProcessor() {
    private val outputFileName = "destination.json"
    private lateinit var messager: Messager
    private lateinit var filer: Filer
    
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        
        messager = processingEnvironment.messager
        filer = processingEnvironment.filer
    }
    
    override fun process(
        set: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        val fragmentElements =
            roundEnvironment.getElementsAnnotatedWith(FragmentDestination::class.java)
        val activityElements =
            roundEnvironment.getElementsAnnotatedWith(ActivityDestination::class.java)
        if (fragmentElements.isNotEmpty() || activityElements.isNotEmpty()) {
            val hashMap = hashMapOf<String, JSONObject>()
            handleProcessor(fragmentElements, FragmentDestination::class.java, hashMap)
            handleProcessor(activityElements, ActivityDestination::class.java, hashMap)
            createFile(hashMap)
        }
        return true
    }
    
    private fun createFile(hashMap: HashMap<String, JSONObject>) {
        val resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", outputFileName)
        val resourcePath = resource.toUri().path
        val appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4)
        val assetsPath = appPath + "src/main/assets/"
        val dir = File(assetsPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val outputFile = File(assetsPath + outputFileName)
        if (outputFile.exists()) {
            outputFile.delete()
        }
        outputFile.createNewFile()
        val jsonString = JSON.toJSONString(hashMap)
        val fileOutputStream = FileOutputStream(outputFile)
        val fileOutputStWriter = OutputStreamWriter(fileOutputStream);
        fileOutputStWriter.write(jsonString)
        fileOutputStWriter.flush()
        fileOutputStWriter.close()
    }
    
    private fun <T : Annotation> handleProcessor(
        fragmentElements: MutableSet<out Element>,
        clazz: Class<T>,
        hashMap: HashMap<String, JSONObject>
    ) {
        for (element in fragmentElements) {
            val typeElement = element as TypeElement
            val className = typeElement.qualifiedName.toString()
            val id = abs(className.hashCode())
            val pageUrl: String
            val needLogin: Boolean
            val asStarter: Boolean
            val isFragment: Boolean
            when (val annotation = typeElement.getAnnotation(clazz)) {
                is FragmentDestination -> {
                    pageUrl = annotation.pageUrl
                    needLogin = annotation.needLogin
                    asStarter = annotation.asStarter
                    isFragment = true
                }
                is ActivityDestination -> {
                    pageUrl = annotation.pageUrl
                    needLogin = annotation.needLogin
                    asStarter = annotation.asStarter
                    isFragment = false
                }
                else -> {
                    pageUrl = ""
                    needLogin = false
                    asStarter = false
                    isFragment = false
                }
            }
            if (hashMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的URL")
                return
            }
            val jsonObject = JSONObject()
            jsonObject["id"] = id
            jsonObject["className"] = className
            jsonObject["pageUrl"] = pageUrl
            jsonObject["needLogin"] = needLogin
            jsonObject["asStarter"] = asStarter
            jsonObject["isFragment"] = isFragment
            hashMap[pageUrl] = jsonObject
        }
    }
}