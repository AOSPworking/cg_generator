# cg_generator

## 1. Usage

用的 IDEA 直接打包。理应得到：

* bcel-6.2.jar;
* cg_generator.jar;
* gson-2.8.5.jar;
* javassist-3.12.1.GA.jar;
* log4j-1.2.17.jar.

总共 5 个 jar 包。

`cg_generator.jar` 同目录下应该有 `io.properties` 以及 `log4j.properties` 两个配置文件。

同时注意 `io.properties` 中对 [ninja_hacked](https://github.com/AOSPworking/ninja-hacked) 以及 [diff_extractor](https://github.com/AOSPworking/diff_extractor) 输出文件的路径配置。（同时也配置 `cg_generator` 的输出）

此后可以直接运行：

```shell script
> java -jar cg_generator.jar
      <path-to-root-about-jar-files-namely-the-prefix>
```

## 2. Description

输出文件一共有两个：

### 2.1 call_graph

标记了整个 jar 包中所有方法的调用关系。以每个类中的方法为节点 (methodMetadata)，给出该节点调用的所有方法 (callingMethods)。

```json
{
  "jarPath": "D:\\tmp\\test-comment\\APIMain.jar",
  "classesInfo": [
    {
      "fullyQualifiedName": "aosp.working.haha.APIMain$APIMainInner",
      "methodsInfo": [
        {
          "methodMetadata": {
            "fullyQualifiedName": "aosp.working.haha.APIMain$APIMainInner.<init>",
            "nonFullyQualifiedName": "<init>",
            "returnType": "void",
            "paramsType": []
          },
          "callingMethods": [
            {
              "fullyQualifiedName": "java.lang.Object.<init>",
              "nonFullyQualifiedName": "<init>",
              "returnType": "void",
              "paramsType": []
            }
          ]
        },
        {
          "methodMetadata": {
            "fullyQualifiedName": "aosp.working.haha.APIMain$APIMainInner.main",
            "nonFullyQualifiedName": "main",
            "returnType": "void",
            "paramsType": [
              "java.lang.String[]"
            ]
          },
          "callingMethods": [
            {
              "fullyQualifiedName": "java.io.PrintStream.println",
              "nonFullyQualifiedName": "println",
              "returnType": "void",
              "paramsType": [
                "int"
              ]
            }
          ]
        }
      ]
    },
    {
      "fullyQualifiedName": "aosp.working.haha.APIMain",
      "methodsInfo": [
        {
          "methodMetadata": {
            "fullyQualifiedName": "aosp.working.haha.APIMain.<init>",
            "nonFullyQualifiedName": "<init>",
            "returnType": "void",
            "paramsType": []
          },
          "callingMethods": [
            {
              "fullyQualifiedName": "java.lang.Object.<init>",
              "nonFullyQualifiedName": "<init>",
              "returnType": "void",
              "paramsType": []
            }
          ]
        },
        {
          "methodMetadata": {
            "fullyQualifiedName": "aosp.working.haha.APIMain.main",
            "nonFullyQualifiedName": "main",
            "returnType": "void",
            "paramsType": [
              "java.lang.String[]"
            ]
          },
          "callingMethods": [
            {
              "fullyQualifiedName": "aosp.working.haha.APIMain.calcu",
              "nonFullyQualifiedName": "calcu",
              "returnType": "int",
              "paramsType": [
                "int",
                "int"
              ]
            },
            {
              "fullyQualifiedName": "java.io.PrintStream.println",
              "nonFullyQualifiedName": "println",
              "returnType": "void",
              "paramsType": [
                "java.lang.String"
              ]
            }
          ]
        },
        {
          "methodMetadata": {
            "fullyQualifiedName": "aosp.working.haha.APIMain.calcu",
            "nonFullyQualifiedName": "calcu",
            "returnType": "int",
            "paramsType": [
              "int",
              "int"
            ]
          },
          "callingMethods": []
        }
      ]
    }
  ]
}
```

### 2.2 center_graph

是 `cg_generator` 的真正输出——根据 [diff_extractor](https://github.com/AOSPworking/diff_extractor) 得到的 “版本间被修改方法名”，得到所有被修改方法为中心节点 (center) 以及所有调用该方法的方法 (methodsCallingThis)。

（因为懒，所以只获取了一层）

```json
{
  "jarPath": "D:\\tmp\\test-comment\\APIMain.jar",
  "callRelationships": [
    {
      "center": {
        "fullyQualifiedName": "aosp.working.haha.APIMain.calcu",
        "nonFullyQualifiedName": "calcu",
        "returnType": "int",
        "paramsType": [
          "int",
          "int"
        ]
      },
      "methodsCallingThis": [
        {
          "fullyQualifiedName": "aosp.working.haha.APIMain.main",
          "nonFullyQualifiedName": "main",
          "returnType": "void",
          "paramsType": [
            "java.lang.String[]"
          ]
        }
      ]
    }
  ]
}
```
