VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub CommandButton1_Click()
    For i = 4 To 1955 Step 1
        Set a = Range("I" & i)
        Set b = Range("K" & i)
        Set c = Range("M" & i)
        Set net = Range("J" & i)
        
        If a.Value <> b.Value And Not (a.Value = "BY" And b.Value = "BL") Then
            Range("B" & i & ":K" & i).Interior.ColorIndex = 37
            Range("N" & i).Value = "Y"
        End If
        
        If a.Value = b.Value And a.Value = "SL" And net.Value <= 0 Then
            Range("B" & i & ":K" & i).Interior.ColorIndex = 36
            Range("O" & i).Value = "Y"
        End If
        
        If b.Value <> c.Value Then
            Range("L" & i & ":M" & i).Interior.ColorIndex = 40
            Range("P" & i).Value = "Y"
        End If
        
    Next i
End Sub
