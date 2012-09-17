VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub export_Click()
    seqNumYOffset = 0
    headerXOffset = 4
    dataXOffset = 5
    dataYOffset = 2
    
    saveDir = Range("G3").Value
    withLock = Range("G4").Value
    
    With Columns("A")
        Set found = .Find(what:="TestCase#:")
    
        If Not found Is Nothing Then
            firstAddress = found.Address
            Do
                delimiter = found.Offset(3, 5).Value
                targetFile = saveDir & "\" & found.Offset(3, 7).Value
                
                allContent = ""
                For i = dataXOffset To 65536 Step 1
                    If IsEmpty(found.Offset(i, seqNumYOffset).Value) Or found.Offset(i, 0).Value = "TestCase#:" Then
                        Exit For
                    End If
                    
                    oneLine = ""
                    For Each ce In Range(found.Offset(i, dataYOffset), found.Offset(i, 256 - found.Column))
                        If ce.Value = "" And found.Offset(headerXOffset, ce.Column - 1).Interior.ColorIndex = xlNone Then
                            Exit For
                        End If
                        
                        If oneLine <> "" Then
                            oneLine = oneLine & delimiter
                        End If
                        oneLine = oneLine & ce.Value
                    Next ce
                    allContent = allContent & oneLine & vbNewLine
                Next i
                
                Open targetFile For Output As #1
                Print #1, allContent
                Close #1
                
                If withLock = "True" Then
                    Open targetFile & ".LOCK" For Output As #1
                    Print #1, ""
                    Close #1
                End If
                
                Set found = .FindNext(found)
            Loop While Not found Is Nothing And found.Address <> firstAddress
        End If
    End With
End Sub
