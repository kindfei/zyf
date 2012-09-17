VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet16"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_FollowHyperlink(ByVal Target As Hyperlink)
    If Not Application.Intersect(Target.Range, Range("S1:S65536")) Is Nothing Then
        posId = Target.Range.Offset(0, -18).Value
        With ActiveSheet
            ActiveSheet.AutoFilterMode = False
            .Range("A1").AutoFilter field:=11, Criteria1:=posId
            .Activate
        End With
    End If
End Sub



